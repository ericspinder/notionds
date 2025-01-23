package com.notionds.dataSource;

import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.WrapperFactory_I;
import com.notionds.dataSource.exceptions.Advice;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
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
    private final CleanupPrepare cleanupPrepare;
    private final Options options;
    /**
     * Holds the ready connection objects, wrapped and active
     */
    protected final BlockingQueue<ConnectionArtifact_I<Connection>> connectionQueue = new LinkedBlockingQueue<>();
    /**
     * The loaned connections are held weakly and will drop out when garbage collected. They will be sent to a
     * referenceQueue in the Cleanup class when ready
     */
    protected final WeakHashMap<ConnectionArtifact_I<Connection>, Instant> loanedConnections = new WeakHashMap<>();
    protected volatile NotionDs.ConnectionSupplier_I activeConnectionSupplier;
    private final Queue<NotionDs.ConnectionSupplier_I> failoverConnectionSuppliers = new ConcurrentLinkedQueue<>();
    public ConnectionPool(WrapperFactory_I wrapperFactoryI, Advice advice, Options options, Queue<NotionDs.ConnectionSupplier_I> connectionSuppliers) {
        this.wrapperFactoryI = wrapperFactoryI;
        this.advice = advice;
        this.options = options;
        this.cleanupPrepare = new CleanupPrepare(this);
        connectionSuppliers.removeIf(connectionSupplierI -> !testAcquireConnection(connectionSupplierI));
        this.activeConnectionSupplier = connectionSuppliers.poll();
        this.failoverConnectionSuppliers.addAll(connectionSuppliers);
    }
    public void shutdown() {
        this.getCleanupPrepare().doCleanup = false;
    }

    public Advice getAdvice() {
        return advice;
    }
    protected void warmPool() {
        logger.trace("warm pool");
        long stamp = connectionGate.writeLock();
        try {
            if (testConnectionSupplier(activeConnectionSupplier)) {
                CompletableFuture.allOf(addConnectionFutures((int) options.get(Options.Integers.Connections_Min_Active.getKey())));
                logger.info("ConnectionQueue size" + connectionQueue.size());
            }
        }
        finally {
            connectionGate.unlockWrite(stamp);
        }
        logger.trace("done warming pool");
    }

    public Connection getConnection() {
        logger.trace("getConnection");
        long read = connectionGate.readLock();
        try {
            try {
                ConnectionArtifact_I<Connection> connectionArtifact = connectionQueue.poll((int) options.get(Options.Integers.Timeout_Retrieve_Connection.getKey()), TimeUnit.SECONDS);
                loanedConnections.put(connectionArtifact, Instant.now().plus((Duration) options.get(Options.Durations.ConnectionTimeoutOnLoan.getKey())));
                return (Connection) connectionArtifact;
            } catch (InterruptedException e) {
                throw new NotionStartupException(NotionStartupException.Type.WAITED_TOO_LONG_FOR_CONNECTION, ConnectionPool.class);
            } finally {
                logger.trace("getConnection - finished");
            }
        }
        finally {
            connectionGate.unlockRead(read);
        }
    }
    public void returnConnection(ConnectionArtifact_I<Connection> connection) {
        logger.trace("return connection " + connection.getArtifactId() + ", currentState = " + connection.getConnectionContainer().getCurrentState());
        this.loanedConnections.remove(connection);
        if (connection.getConnectionContainer().getCurrentState().equals(State.Closed) || connection.getConnectionContainer().getCurrentState().equals(State.Empty)) {
            try {
                connection.getDelegate().close();
            } catch (SQLException e) {
                logger.error("Problem closing a connection which had currentState set to close, ignoring - ArtifactId = " + connection.getArtifactId());
            }
            return;
        }
        if (connection.getConnectionContainer().getCurrentState().equals(State.Open)) {
            connection.getConnectionContainer().currentState = State.Pooled;
            this.connectionQueue.add(connection);
            return;
        }
    }

    private void addConnection(NotionDs.ConnectionSupplier_I connectionSupplier) {
        try {
            Connection rawConnection = connectionSupplier.getConnection();
            ConnectionArtifact_I<Connection> added = wrapperFactoryI.getDelegate(null, rawConnection, Connection.class);
            added.setConnectionContainer(new ConnectionContainer(connectionSupplier.getUUID(), added, this, this.wrapperFactoryI));
            this.connectionQueue.add(added);
            logger.info("ConnectionId=" + added.getArtifactId() + " was added to connection pool, queue_size =" + connectionQueue.size() + ", loaned = " + loanedConnections.size());
        } catch (SQLException e) {
            logger.error("Error creating ");
            this.throwBackProcessedException(e,null);
            this.doFailover(connectionSupplier.getUUID(),false);
        }
    }
    public void processException(NotionExceptionWrapper wrappedException, ConnectionArtifact_I<?> connectionArtifact) {
        logger.error("process exception - " + wrappedException.getMessage() + " cause - " + wrappedException.getCause().getMessage() + ", recommendation = " + wrappedException.getRecommendation().toString());
        if (wrappedException.getRecommendation().shouldClose() && connectionArtifact != null) {
            logger.info("Close delegate");
            try {
                ((Closeable) connectionArtifact.getDelegate()).close();
            }
            catch (IOException ioe) {
                logger.error("exception in close for ArtifactId = " + connectionArtifact.getArtifactId());
            }
        }
        if (wrappedException.getRecommendation().isFailoverToNextConnectionSupplier()) {
            logger.info("Failover");
            assert connectionArtifact != null;
            this.doFailover(connectionArtifact.getConnectionContainer().getGetConnectionSupplierUUID(),false);
        }
    }
    /**
     * Handles the exception, then rethrows the 'real' exception
     * @param cause
     * @throws Throwable
     */
    public NotionExceptionWrapper throwBackProcessedException(Throwable cause, ConnectionArtifact_I<?> connectionArtifact) {
        logger.error("throwback " + cause.getCause() + ", connectionArtifact = " + ((connectionArtifact != null)?connectionArtifact.getArtifactId():"null"));
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
     * @param burnPreviousConnections clears connections which may still be active from a previous connection supplier
     */
    public void doFailover(UUID offendingConnectionUUID, Boolean burnPreviousConnections) {
        if (offendingConnectionUUID.equals(activeConnectionSupplier.getUUID())) {
            long stamp = connectionGate.writeLock();
            logger.error("do failover - burn = " + burnPreviousConnections + " current login = " + activeConnectionSupplier.getUUID());
            try {
                if (offendingConnectionUUID.equals(activeConnectionSupplier.getUUID())) {
                    NotionDs.ConnectionSupplier_I failoverConnection = this.failoverConnectionSuppliers.poll();
                    if (failoverConnection != null) {
                        this.activeConnectionSupplier = failoverConnection;
                        if (burnPreviousConnections) {
                            this.emptyAllOldConnections(activeConnectionSupplier.getUUID());
                        }
                        CompletableFuture.allOf(addConnectionFutures((int) options.get(Options.Integers.Connections_Min_Active.getKey())));
                    } else {
                        throw new NotionStartupException(NotionStartupException.Type.No_Failover_Available, this.getClass());
                    }
                    logger.error("failover Connection engaged UUID = " + failoverConnection.getUUID());
                }
            } finally {
                connectionGate.unlockWrite(stamp);
                if (!this.testConnectionSupplier(activeConnectionSupplier)) {
                    if (!this.failoverConnectionSuppliers.isEmpty()) {
                        doFailover(activeConnectionSupplier.getUUID(),false);
                    }
                }
            }
        }
    }

    public CompletableFuture<?>[] addConnectionFutures(int number) {
        List<CompletableFuture<?>> completableFutures = new ArrayList<>();
        if (number > 0) {
            for (int i = 0; i < number; i++) {
                completableFutures.add(CompletableFuture.runAsync(() -> {
                    this.addConnection(this.activeConnectionSupplier);
                }));
            }
        }
        else {
            logger.error("Cannot add 0 or less connections");
        }
        return completableFutures.toArray(new CompletableFuture<?>[0]);
    }

    /**
     * Drains and closes the current connection pool and marks them all loaned to be close when no longer in use, rather than returned to the pool.
     */
    public void emptyAllOldConnections(UUID newConnectionSupplierUUID) {
        for (ConnectionContainer connectionContainer: this.cleanupPrepare.timeoutCleanup.keySet()) {
            if (!connectionContainer.getGetConnectionSupplierUUID().equals(newConnectionSupplierUUID)) {
                connectionContainer.currentState = State.Empty;
            }
        }
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

    public CleanupPrepare getCleanupPrepare() {
        return this.cleanupPrepare;
    }
    /**
     * A connection test which will lock out the normal 'acquireConnection' method
     * @return
     */
    public boolean testAcquireConnection(NotionDs.ConnectionSupplier_I connectionSupplier) {
        long writeLock = connectionGate.writeLock();
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

