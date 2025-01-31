package com.notionds.dataSource;

import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.WrapperFactory_I;
import com.notionds.dataSource.exceptions.Advice;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.StampedLock;

public class ConnectionPool {

    private static final Logger logger = LogManager.getLogger();
    private final StampedLock connectionGate = new StampedLock();
    private final WrapperFactory_I wrapperFactoryI;
    private final Advice advice;
    private final Prepare prepare;
    private final Options options;

    /**
     * Holds the ready connection objects, wrapped and active
     */
    protected final BlockingQueue<ConnectionArtifact_I<Connection>> connectionQueue = new LinkedBlockingQueue<>();
    protected final List<ConnectionContainer> activeConnectionContainers = new ArrayList<>();
    protected volatile NotionDs.ConnectionSupplier_I activeConnectionSupplier;
    private final Queue<NotionDs.ConnectionSupplier_I> failoverConnectionSuppliers = new ConcurrentLinkedQueue<>();
    public ConnectionPool(WrapperFactory_I wrapperFactoryI, Advice advice, Options options, Queue<NotionDs.ConnectionSupplier_I> connectionSuppliers) {
        this.wrapperFactoryI = wrapperFactoryI;
        this.advice = advice;
        this.options = options;
        this.prepare = new Prepare(this);
        connectionSuppliers.removeIf(connectionSupplierI -> !testAcquireConnection(connectionSupplierI));
        if (connectionSuppliers.isEmpty()) {
            throw new RuntimeException("Connection suppliers is empty");
        }
        this.activeConnectionSupplier = connectionSuppliers.poll();
        this.failoverConnectionSuppliers.addAll(connectionSuppliers);
    }
    public void shutdown() {
        this.getCleanupPrepare().doCleanup = false;
    }

    protected boolean warmPool() {
        logger.trace("warm pool");
        long stamp = connectionGate.writeLock();
        try {
            if (testConnectionSupplier(activeConnectionSupplier)) {
                CompletableFuture.allOf(addConnectionFutures((int) options.get(Options.Integers.Connections_Min_Active.getKey()))).get();
                logger.info("ConnectionQueue size = " + connectionQueue.size());
                return true;
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            connectionGate.unlockWrite(stamp);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public Connection getConnection() {
        try {
            Connection connection = (Connection) connectionQueue.poll((int) options.get(Options.Integers.Timeout_Retrieve_Connection.getKey()), TimeUnit.MILLISECONDS);
            ConnectionContainer connectionContainer = ((ConnectionArtifact_I<Connection>) Objects.requireNonNull(connection)).getConnectionContainer();
            connectionContainer.currentState = State.Loaned;
            return connection;
        } catch (InterruptedException | NullPointerException e) {
            logger.info("Adding connection in process rather than failing");
            return this.createNewConnection(this.activeConnectionSupplier);
        }

    }

    @SuppressWarnings("unchecked")
    public void returnConnection(Connection connection, ConnectionContainer connectionContainer, String process) {
        try {
            if (connectionContainer.currentState.equals(State.Pooled) || connectionContainer.getCurrentState().equals(State.Empty) || Instant.now().isAfter(connectionContainer.maxLifetimeExpireInstant) || activeConnectionContainers.size() > (Integer) options.get(Options.Integers.Connection_Max_Queue_Size.getKey()) ) {
                activeConnectionContainers.remove(connectionContainer);
            }
            if (activeConnectionContainers.size() <= ((Integer)options.get(Options.Integers.Connection_Max_Queue_Size.getKey())) && connectionContainer.getCurrentState().equals(State.Loaned)) {
                Connection wrappedConnection = this.wrapperFactoryI.getDelegate(connectionContainer, connection, Connection.class);
                connectionContainer.currentState = State.Pooled;
                this.connectionQueue.add((ConnectionArtifact_I<Connection>) wrappedConnection);
            }
        } catch (NullPointerException npe) {
            logger.info("Missing connection from ConnectionContainer = " + connectionContainer.containerId + ", process = " + process);
        }
    }

    @SuppressWarnings("unchecked")
    private void addConnection(NotionDs.ConnectionSupplier_I connectionSupplier) {
        this.connectionQueue.add((ConnectionArtifact_I<Connection>)createNewConnection(connectionSupplier));
    }
    private synchronized Connection createNewConnection(NotionDs.ConnectionSupplier_I connectionSupplier) {
        try {
            Connection rawConnection = connectionSupplier.getConnection();
            ConnectionContainer connectionContainer = new ConnectionContainer(connectionSupplier.getUUID(), this, this.wrapperFactoryI,(Duration) options.get(Options.Durations.ConnectionMaxLifetime.getKey()));
            Connection created = wrapperFactoryI.getDelegate(connectionContainer, rawConnection, Connection.class);
            this.activeConnectionContainers.add(connectionContainer);
            logger.info("ConnectionContainerId=" + connectionContainer.containerId + " was added to connection pool, queue_size =" + connectionQueue.size() + ", total = " + activeConnectionContainers.size());
            return created;
        } catch (SQLException e) {
            logger.error("Error creating ");
            this.throwBackProcessedException(e,null);
            this.doFailover(connectionSupplier.getUUID());
            throw new RuntimeException(e);
        }
    }
    public void processException(NotionExceptionWrapper wrappedException, ConnectionArtifact_I<?> connectionArtifact) {
        logger.error("process exception - " + wrappedException.getMessage() + " cause - " + wrappedException.getCause().getMessage() + ", recommendation = " + wrappedException.getRecommendation().toString());
        if (connectionArtifact != null) {
            connectionArtifact.getConnectionContainer().currentState = State.Empty;
        }
        if (wrappedException.getRecommendation().isFailoverToNextConnectionSupplier()) {
            logger.info("failover needed for " + wrappedException.getCause().getMessage());
            assert connectionArtifact != null;
            this.doFailover(connectionArtifact.getConnectionContainer().getGetConnectionSupplierUUID());
        }
    }
    /**
     * Handles the exception, then rethrows the 'real' exception
     * @param cause the cause
     */
    public NotionExceptionWrapper throwBackProcessedException(Throwable cause, ConnectionArtifact_I<?> connectionArtifact) {
        logger.error("throwback " + cause.getMessage() + ", connectionContainer = " + ((connectionArtifact != null)? connectionArtifact.getConnectionContainer().containerId:"null"));
        NotionExceptionWrapper wrappedException = switch (cause) {
            case SQLClientInfoException throwable -> this.advice.adviseSQLClientInfoException(throwable);
            case SQLException throwable -> this.advice.adviseSqlException(throwable);
            case IOException ioException -> this.advice.adviseIoException(ioException);
            case Exception exception -> this.advice.adviseException(exception);
            default -> this.advice.adviseThrowable(cause);
        };
        if (wrappedException != null && connectionArtifact != null) {
            this.processException(wrappedException,connectionArtifact);
        }
        return wrappedException;
    }
    /**
     * Updates the active connection supplier
     * @param offendingConnectionUUID is the connection id which failed
     */
    public void doFailover(UUID offendingConnectionUUID) {
        if (offendingConnectionUUID == null || offendingConnectionUUID.equals(activeConnectionSupplier.getUUID())) {
            long stamp = connectionGate.writeLock();
            logger.error("do failover - current login = " + activeConnectionSupplier.getUUID() + ", offending login = " + offendingConnectionUUID);
            try {
                if (offendingConnectionUUID == null || offendingConnectionUUID.equals(activeConnectionSupplier.getUUID())) {
                    NotionDs.ConnectionSupplier_I failoverConnection = findWorkingFailover();
                    if (failoverConnection != null) {
                        this.activeConnectionSupplier = failoverConnection;
                        CompletableFuture.allOf(addConnectionFutures((int) options.get(Options.Integers.Connections_Min_Active.getKey()))).get();
                    }
                    assert failoverConnection != null;
                    logger.error("failover Connection engaged UUID = " + failoverConnection.getUUID());
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                connectionGate.unlockWrite(stamp);
            }
        }
    }
    private NotionDs.ConnectionSupplier_I findWorkingFailover() {
        while (!failoverConnectionSuppliers.isEmpty()) {
            NotionDs.ConnectionSupplier_I failoverConnection = failoverConnectionSuppliers.poll();
            if (testConnectionSupplier(failoverConnection)) {
                return failoverConnection;
            }
        }
        throw new NotionStartupException(NotionStartupException.Type.No_Failover_Available, this.getClass());
    }

    public CompletableFuture<?>[] addConnectionFutures(int number) {
        List<CompletableFuture<?>> completableFutures = new ArrayList<>();
        if (number > 0) {
            for (int i = 0; i < number; i++) {
                completableFutures.add(CompletableFuture.runAsync(() -> this.addConnection(this.activeConnectionSupplier)));
            }
        }
        else {
            logger.error("Cannot add 0 or less connections");
        }
        return completableFutures.toArray(new CompletableFuture<?>[0]);
    }

    /**
     * Add a connection supplier to the failover stack
     * @param failoverConnectionSupplier the new failover connection
     */
    public void addFailover(NotionDs.ConnectionSupplier_I failoverConnectionSupplier) {
        if (this.testConnectionSupplier(failoverConnectionSupplier)) {
            this.failoverConnectionSuppliers.offer(failoverConnectionSupplier);
        }
        else {
            logger.error("new failover connection failed it's test, it will not be added: ConnectionSupplierUUID = " + failoverConnectionSupplier.getUUID());
        }
    }

    public Prepare getCleanupPrepare() {
        return this.prepare;
    }
    /**
     * A connection test which will lock out the normal 'acquireConnection' method
     * @return boolean if the connection succeeded
     */
    public boolean testAcquireConnection(NotionDs.ConnectionSupplier_I connectionSupplier) {
        long writeLock = connectionGate.writeLock();
        logger.info("testing connectionSupplier UUID = " + connectionSupplier.getUUID() + ", testSQL = " + connectionSupplier.getTestSQL());
        try {
            return testConnectionSupplier(connectionSupplier);
        }
        finally {
            connectionGate.unlockWrite(writeLock);
        }
    }
    private boolean testConnectionSupplier(NotionDs.ConnectionSupplier_I connectionSupplier) {
        try {
            Connection connection = connectionSupplier.getConnection();
            if (connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(connectionSupplier.getTestSQL());
                boolean result = preparedStatement.execute();
                connection.close();
                return result;
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
        }
        return false;
    }

    public Options getOptions() {
        return options;
    }
}

