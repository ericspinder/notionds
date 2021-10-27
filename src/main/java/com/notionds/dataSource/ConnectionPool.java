package com.notionds.dataSource;

import com.notionds.dataSource.connection.Cleanup;
import com.notionds.dataSource.connection.Container;
import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static com.notionds.dataSource.Options.NotionDefaultDuration.*;
import static com.notionds.dataSource.Options.NotionDefaultIntegers.*;
import static com.notionds.dataSource.Options.*;
public abstract class ConnectionPool<O extends Options> {

    @SuppressWarnings("unchecked")
    public static class Default extends ConnectionPool {
        public Default(Executor connectionFuture, Queue<NotionDs.ConnectionSupplier_I> connectionSuppliers) {
            super(
                    DEFAULT_OPTIONS_INSTANCE,
                    connectionFuture,
                    (Duration) DEFAULT_OPTIONS_INSTANCE.get(ConnectionMaxLifetime.getKey()).getValue(),
                    (Duration) DEFAULT_OPTIONS_INSTANCE.get(ConnectionTimeoutOnLoan.getKey()).getValue(),
                    (Duration) DEFAULT_OPTIONS_INSTANCE.get(ConnectionTimeoutInPool.getKey()).getValue(),
                    (int) DEFAULT_OPTIONS_INSTANCE.get(Connection_Max_Queue_Size.getKey()).getValue(),
                    (int) DEFAULT_OPTIONS_INSTANCE.get(Connections_Min_Active.getKey()).getValue(),
                    connectionSuppliers
            );
        }
    }
    private static final Logger log = LogManager.getLogger();
    protected final O options;
    private final Executor connectionFutures;

    private final Cleanup<?> cleanup;
    /**
     * The max time a connection will be allowed to stay active.
     */
    private Duration maxConnectionLifetime;
    /**
     * Default timeout when loaned out
     */
    private Duration timeoutOnLoan_default;
    /**
     * This is the length of time a client will wait for a connection before erring out
     * The Duration is split into TimeUnits for efficient use in the poll method
     */
    private long connection_retrieve_millis;
    private final TimeUnit connection_retrieve_time_unit = TimeUnit.MILLISECONDS;
    /**
     * Max number of connections allowed, this is not a hard limit
     */
    private int maxTotalAllowedConnections;
    /**
     * The number of connections the system will attempt to keep in absence of breaching the maximum
     */
    private int minActiveConnections;
    /**
     * Holds the ready connection objects, wrapped and active
     */
    private final BlockingQueue<ConnectionArtifact_I> connectionQueue = new LinkedBlockingQueue<>();
    /**
     * The loaned connections are held weakly and will drop out when garbage collected. They will be sent to a
     * referenceQueue in the Cleanup class when ready
     */
    private final WeakHashMap<ConnectionArtifact_I, Instant> loanedConnections = new WeakHashMap<>();
    private volatile NotionDs.ConnectionSupplier_I connectionSupplier;
    private final Queue<NotionDs.ConnectionSupplier_I> failoverConnectionSuppliers = new ConcurrentLinkedQueue<>();

    public ConnectionPool(O options, Executor connectionFutures, Duration maxConnectionLifetime, Duration timeoutOnLoan_default, Duration connection_retrieve, int maxTotalAllowedConnections, int minActiveConnections, Queue<NotionDs.ConnectionSupplier_I> connectionSuppliers) {
        this.options = options;
        this.connectionFutures = connectionFutures;
        this.maxConnectionLifetime = maxConnectionLifetime;
        this.timeoutOnLoan_default = timeoutOnLoan_default;
        this.connection_retrieve_millis = connection_retrieve_time_unit.convert(connection_retrieve);
        this.maxTotalAllowedConnections = maxTotalAllowedConnections;
        this.minActiveConnections = minActiveConnections;
        this.connectionSupplier = connectionSuppliers.poll();
        this.failoverConnectionSuppliers.addAll(connectionSuppliers);
        this.cleanup = new Cleanup<>(options, this::returnConnectionFuture, this::doFailover);
    }
    protected ConnectionArtifact_I populateConnectionContainer(Container<?,?,?> container) {
        try {
            return container.wrap(this.connectionSupplier.getConnection(), Connection.class, null);
        } catch (SQLException e) {
            container.handleSQLException(e, null);
            return null;
        }
    }

    private boolean evalForSpaceInConnectionQueue() {
        return loanedConnections.size() < (maxTotalAllowedConnections + minActiveConnections) && connectionQueue.size() < minActiveConnections;
    }
    public ConnectionArtifact_I getConnection(Supplier<ConnectionArtifact_I> newConnectionArtifactSupplier) {
        if (evalForSpaceInConnectionQueue()) {
            addConnectionFutures(newConnectionArtifactSupplier,minActiveConnections - connectionQueue.size());
        }
        try {
            ConnectionArtifact_I connection = connectionQueue.poll(connection_retrieve_millis, connection_retrieve_time_unit);
            loanedConnections.put(connection, Instant.now());
            return connection;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new NotionStartupException(NotionStartupException.Type.WAITED_TOO_LONG_FOR_CONNECTION, ConnectionPool.class);
        }
    }

    public boolean addConnection(ConnectionArtifact_I added, boolean returned) {
        if (added.getContainer().getConnection().equals(added)) {
            if (returned) {
                this.loanedConnections.remove(added);
                if (!added.getContainer().reuse(added)) {
                    log.error("ConnectionId=" + added.getContainer().containerId + " was not able to reuse, will close");
                    added.getContainer().closeDelegate(added);
                    return false;
                }
            }
            if (evalForSpaceInConnectionQueue() && connectionQueue.add(added)) {
                added.getContainer().currentState = State.Pooled;
                log.trace("ConnectionId=" + added.getContainer().containerId + " was added/re-added to connection pool queue_size=" + connectionQueue.size());
                return true;
            } else {
                log.error("ConnectionId=" + added.getContainer().containerId + " was not able to add connection pool due to queue_size=" + connectionQueue.size());
                added.getContainer().closeDelegate(added);
                return false;
            }
        }
        else {
            // child object to be closed (may also be a Connection, but it would not be properly wrapped in its own ConnectionContainer)
            added.getContainer().closeDelegate(added);
            return false;
        }
    }
    /**
     * Updates the active connection supplier
     * @param burn clears connections which may still be active from previous connection supplier
     */
    public void doFailover(Boolean burn) {
        NotionDs.ConnectionSupplier_I failoverConnection = this.failoverConnectionSuppliers.poll();
        if (failoverConnection != null) {
            this.connectionSupplier = failoverConnection;
            if (burn) {
                this.drainAllCurrentConnections();
            }
        }
        else {
            throw new NotionStartupException(NotionStartupException.Type.No_Failover_Available, this.getClass());
        }
    }

    public void addConnectionFutures(Supplier<ConnectionArtifact_I> newConnectionSupplier, int number) {
        if (number > 0) {
            for (int i = 0; i < number; i++) {
                CompletableFuture.supplyAsync(() -> this.addConnection(newConnectionSupplier.get(), false), connectionFutures);
            }
        }
        else {
            log.info("This sort of threading failure should be rare overall, maybe?");
        }
    }
    public void returnConnectionFuture(ConnectionArtifact_I added) {
        CompletableFuture.supplyAsync(()-> this.addConnection(added, true), connectionFutures);
    }

    /**
     * Drains and closes the current connection pool and marks them all loaned to be close when no longer in use, rather than returned to the pool.
     */
    public void drainAllCurrentConnections() {
        List<ConnectionArtifact_I> drain = new ArrayList<>();
        this.connectionQueue.drainTo(drain);
        this.loanedConnections.keySet().stream().forEach((ConnectionArtifact_I loaned) -> loaned.getContainer().currentState = State.Empty);
        drain.forEach((ConnectionArtifact_I artifactI) -> artifactI.getContainer().closeDelegate(artifactI));

    }

    /**
     * Add a connection supplier to the failover stack
     * @param failoverConnectionSupplier
     */
    public void addFailover(NotionDs.ConnectionSupplier_I failoverConnectionSupplier) {
        this.failoverConnectionSuppliers.offer(failoverConnectionSupplier);
    }

    public Duration getMaxConnectionLifetime() {
        return maxConnectionLifetime;
    }

    public void setMaxConnectionLifetime(Duration maxConnectionLifetime) {
        this.maxConnectionLifetime = maxConnectionLifetime;
    }

    public Duration getTimeoutOnLoan_default() {
        return timeoutOnLoan_default;
    }

    public void setTimeoutOnLoan_default(Duration timeoutOnLoan_default) {
        this.timeoutOnLoan_default = timeoutOnLoan_default;
    }

    public Duration getConnection_retrieveTimeout() {
        return Duration.of(connection_retrieve_millis, connection_retrieve_time_unit.toChronoUnit());
    }

    public void setConnection_retrieveTimeout(Duration connection_retrieveTimeout) {
        this.connection_retrieve_time_unit.convert(connection_retrieveTimeout);
    }

    public int getMaxTotalAllowedConnections() {
        return maxTotalAllowedConnections;
    }

    public void setMaxTotalAllowedConnections(int maxTotalAllowedConnections) {
        this.maxTotalAllowedConnections = maxTotalAllowedConnections;
    }

    public int getMinActiveConnections() {
        return minActiveConnections;
    }

    public void setMinActiveConnections(int minActiveConnections) {
        this.minActiveConnections = minActiveConnections;
    }
    public Cleanup<?> getCleanup() {
        return this.cleanup;
    }
}
