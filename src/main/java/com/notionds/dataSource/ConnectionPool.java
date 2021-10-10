package com.notionds.dataSource;

import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

public abstract class ConnectionPool<O extends Options> {

    public static final ConnectionPool.Default DEFAULT_INSTANCE = new Default(new ForkJoinPool(10), new ForkJoinPool(10));

    @SuppressWarnings("unchecked")
    public static class Default extends ConnectionPool {
        public Default(Executor executor1, Executor executor2) {
            super(
                    Options.DEFAULT_INSTANCE,
                    executor1,
                    executor2,
                    (Duration) Options.DEFAULT_INSTANCE.get(Options.NotionDefaultDuration.ConnectionMaxLifetime.getKey()).getValue(),
                    (Duration) Options.DEFAULT_INSTANCE.get(Options.NotionDefaultDuration.ConnectionTimeoutOnLoan.getKey()).getValue(),
                    (Duration) Options.DEFAULT_INSTANCE.get(Options.NotionDefaultDuration.ConnectionTimeoutInPool.getKey()).getValue(),
                    (int) Options.DEFAULT_INSTANCE.get(Options.NotionDefaultIntegers.Connection_Max_Wait_On_Create.getKey()).getValue(),
                    (int) Options.DEFAULT_INSTANCE.get(Options.NotionDefaultIntegers.Connection_Max_Queue_Size.getKey()).getValue(),
                    (int) Options.DEFAULT_INSTANCE.get(Options.NotionDefaultIntegers.Connections_Min_Active.getKey()).getValue()
            );
        }

    }
    private static final Logger log = LogManager.getLogger();
    protected final O options;
    private final Executor executor1;
    private final Executor executor2;
    private final Duration maxConnectionLifetime;
    private final Duration timeoutOnLoan_default;
    private final Duration timeoutInPool;
    private final long millis;
    private final int maxQueueSize;
    private final int minActiveConnections;
    /**
     * Holds the ready connection objects, wrapped and active
     */
    private final BlockingQueue<ConnectionArtifact_I> connectionQueue = new LinkedBlockingQueue<>();
    /**
     * The loaned connections are held weakly and will drop out when garbage collected.
     */
    private final WeakHashMap<ConnectionArtifact_I, Instant> loanedConnections = new WeakHashMap<>();

    public ConnectionPool(O options, Executor executor1, Executor executor2, Duration maxConnectionLifetime, Duration timeoutOnLoan_default, Duration timeoutInPool, long millis, int maxQueueSize, int minActiveConnections) {
        this.options = options;
        this.executor1 = executor1;
        this.executor2 = executor2;
        this.maxConnectionLifetime = maxConnectionLifetime;
        this.timeoutOnLoan_default = timeoutOnLoan_default;
        this.timeoutInPool = timeoutInPool;
        this.millis = millis;
        this.maxQueueSize = maxQueueSize;
        this.minActiveConnections = minActiveConnections;
    }

    public ConnectionArtifact_I getConnection(Supplier<ConnectionArtifact_I> newConnectionArtifactSupplier) {
        if (loanedConnections.size() < (maxQueueSize + minActiveConnections) && connectionQueue.size() < minActiveConnections) {
            addConnectionFutures(newConnectionArtifactSupplier,minActiveConnections - connectionQueue.size());
        }
        try {
            ConnectionArtifact_I connection = connectionQueue.poll(millis, TimeUnit.MILLISECONDS);
            loanedConnections.put(connection, Instant.now());
            return connection;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new NotionStartupException(NotionStartupException.Type.WAITED_TOO_LONG_FOR_CONNECTION, ConnectionPool.class);
        }
    }

    public boolean addConnection(ConnectionArtifact_I added, boolean returned) {
        if (added.getConnectionMain().getConnection().equals(added)) {
            if (returned) {
                this.loanedConnections.remove(added);
                if (added.getConnectionMain().reuse(added)) {
                    added.getConnectionMain().currentState = State.Pooled;
                } else {
                    log.error("ConnectionId=" + added.getConnectionMain().connectionId + " was not able to reuse, will close");
                    added.closeDelegate();
                    return false;
                }
            }
            if (connectionQueue.add(added)) {
                log.trace("ConnectionId=" + added.getConnectionMain().connectionId + " was re-added to connection queue queue_size=" + connectionQueue.size());
                return true;
            } else {
                log.error("ConnectionId=" + added.getConnectionMain().connectionId + " was not able to add connection due to queue_size=" + connectionQueue.size());
                added.closeDelegate();
                return false;
            }
        }
        else {
            // child object to be closed (may also be a connection, but it would not be properly wrapped in its own ConnectionContainer)
            added.closeDelegate();
            return false;
        }

    }

    public void addConnectionFutures(Supplier<ConnectionArtifact_I> newConnectionSupplier, int number) {
        if (number > 0) {
            CompletableFuture.supplyAsync(() -> {
                List<CompletableFuture<Boolean>> futures = new ArrayList<>();
                for (int i = 0; i < number; i++) {
                    futures.add(CompletableFuture.supplyAsync(() -> this.addConnection(newConnectionSupplier.get(), false), executor2));
                }
                return futures;
            }, executor1);
        }
        else {
            log.info("This sort of threading failure should be rare overall, maybe?");
        }
    }
    public void addConnectionFuture(ConnectionArtifact_I added, boolean returned) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(()-> this.addConnection(added, returned), executor2);
        return;
    }

}
