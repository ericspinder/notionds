package com.notionds.dataSource;

import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.WrapperFactory_I;
import com.notionds.dataSource.connection.delegation.jdbcProxy.logging.LoggingWrapperFactory;
import com.notionds.dataSource.exceptions.Advice;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;
import com.notionds.dataSource.exceptions.Recommendation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
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

    private final Cleanup cleanup;

    private final Options options;
    /**
     * Holds the ready connection objects, wrapped and active
     */
    private final BlockingQueue<ConnectionArtifact_I<Connection>> connectionQueue = new LinkedBlockingQueue<>();
    /**
     * The loaned connections are held weakly and will drop out when garbage collected. They will be sent to a
     * referenceQueue in the Cleanup class when ready
     */
    private final WeakHashMap<ConnectionArtifact_I<Connection>, Instant> loanedConnections = new WeakHashMap<>();
    private volatile NotionDs.ConnectionSupplier_I activeConnectionSupplier;
    private final Queue<NotionDs.ConnectionSupplier_I> failoverConnectionSuppliers = new ConcurrentLinkedQueue<>();
    public ConnectionPool(WrapperFactory_I wrapperFactoryI, Advice advice, Options options, Queue<NotionDs.ConnectionSupplier_I> connectionSuppliers) {
        this.wrapperFactoryI = wrapperFactoryI;
        this.advice = advice;
        this.options = options;
        this.activeConnectionSupplier = connectionSuppliers.poll();
        this.failoverConnectionSuppliers.addAll(connectionSuppliers);
        this.cleanup = new Cleanup();
        if (!this.testAcquireConnection()) {
            doFailover(false);
        };
    }
    public void shutdown() {
        this.getCleanup().doCleanup = false;
    }

    public Advice getAdvice() {
        return advice;
    }
    protected void warmPool() {
        logger.trace("warm pool");
        long stamp = connectionGate.writeLock();
        try {
            getConnection();
        }
        finally {
            connectionGate.unlockWrite(stamp);
        }
        logger.trace("done warming pool");
    }

    private boolean evalForSpaceInConnectionQueue() {
        return loanedConnections.size() + connectionQueue.size() < (int) options.get(Options.Integers.Connection_Max_Queue_Size.getKey());
    }
    public Connection getConnection() {
        logger.trace("getConnection");
        if (evalForSpaceInConnectionQueue()) {
            logger.trace("adding connections");
            CompletableFuture.allOf(addConnectionFutures(activeConnectionSupplier,(int) options.get(Options.Integers.Connections_Min_Active.getKey()) - connectionQueue.size()));
        }
        try {
            ConnectionArtifact_I<Connection> connectionArtifact = connectionQueue.poll((int) options.get(Options.Integers.Timeout_Retrieve_Connection.getKey()),TimeUnit.SECONDS);
            loanedConnections.put(connectionArtifact, Instant.now().plus((Duration)options.get(Options.Durations.ConnectionTimeoutOnLoan.getKey())));
            return (Connection) connectionArtifact;
        } catch (InterruptedException e) {
            throw new NotionStartupException(NotionStartupException.Type.WAITED_TOO_LONG_FOR_CONNECTION, ConnectionPool.class);
        }
        finally {
            logger.trace("getConnection - finished");
        }

    }
    public void returnConnection(ConnectionArtifact_I<Connection> connection) {
        logger.trace("return connection");
        this.loanedConnections.remove(connection);
        if (connection.getConnectionContainer().getCurrentState().equals(State.Closed) || connection.getConnectionContainer().getCurrentState().equals(State.Empty)) {
            logger.debug("ConnectionId=" + connection.getConnectionContainer().containerId + " was not able to reuse, will close");
            return;
        }
        if (connection.getConnectionContainer().getCurrentState().equals(State.Open)) {
            connection.getConnectionContainer().currentState = State.Pooled;
            this.connectionQueue.add(connection);
            return;
        }

    }
    private void addConnectionFuture(NotionDs.ConnectionSupplier_I connectionSupplier) {
        logger.trace("doing completable future ");
        try {
            this.addConnection(connectionSupplier.getConnection());
        } catch (SQLException e) {
            this.throwBackProcessedException(e,null);
        }

    }

    private void addConnection(Connection connection) {
        logger.trace("adding connection");
        if (evalForSpaceInConnectionQueue()) {
            logger.trace("got space to add");
            ConnectionArtifact_I<Connection> added = wrapperFactoryI.getDelegate(null,connection,Connection.class);
            logger.trace("connectionArtifact " + added.getArtifactId());
            added.setConnectionContainer(new ConnectionContainer(added,this,this.wrapperFactoryI,(Duration) this.options.get(Options.Durations.ConnectionChildTimeout.getKey())));
            connectionQueue.add(added);
            logger.trace("ConnectionId=" + added.getArtifactId() + " was added/re-added to connection pool queue_size=" + connectionQueue.size());
        } else {
            logger.error("Connection was not able to add connection pool due to queue_size=" + connectionQueue.size());
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Exception trying to close extra connection: " + e.getMessage());
            }
        }

    }
    public void processException(NotionExceptionWrapper wrappedException) {
        logger.error("process exception - " + wrappedException.getMessage() + "cause - " + wrappedException.getCause().getMessage());
        ConnectionArtifact_I<?> connectionArtifact = wrappedException.getConnectionArtifact();
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
            this.doFailover(true);
        }
    }
    /**
     * Handles the exception, then rethrows the 'real' exception
     * @param cause
     * @throws Throwable
     */
    public NotionExceptionWrapper throwBackProcessedException(Throwable cause, ConnectionArtifact_I<?> connectionArtifact) {
        logger.trace("throwBackProcessException");
        if (cause != null) {
            NotionExceptionWrapper wrappedException = switch (cause) {
                case SQLClientInfoException throwable ->
                        this.advice.adviseSQLClientInfoException(throwable, connectionArtifact);
                case SQLException throwable -> this.advice.adviseSqlException(throwable, connectionArtifact);
                case IOException ioException -> this.advice.adviseIoException(ioException, connectionArtifact);
                case Exception exception -> this.advice.adviseException(exception, connectionArtifact);
                default -> this.advice.adviseThrowable(cause, connectionArtifact);
            };
            if (wrappedException != null) {
                this.processException(wrappedException);
            }
            return wrappedException;
        }
        throw new RuntimeException("No cause");
    }
    /**
     * Updates the active connection supplier
     * @param burn clears connections which may still be active from previous connection supplier
     */
    public void doFailover(Boolean burn) {
        long stamp = connectionGate.writeLock();
        logger.info("do failover - burn = " + burn + " current login = " + activeConnectionSupplier.getUUID());
        try {
            NotionDs.ConnectionSupplier_I failoverConnection = this.failoverConnectionSuppliers.poll();
            if (failoverConnection != null) {
                if (burn) {
                    this.drainAllCurrentConnections();
                }
                this.activeConnectionSupplier = failoverConnection;
            } else {
                throw new NotionStartupException(NotionStartupException.Type.No_Failover_Available, this.getClass());
            }
            logger.info("failover Connection engaged UUID = " + failoverConnection.getUUID());
        }
        finally {
            connectionGate.unlockWrite(stamp);
        }
    }

    public CompletableFuture<?>[] addConnectionFutures(NotionDs.ConnectionSupplier_I connectionSupplier, int number) {
        List<CompletableFuture<?>> completableFutures = new ArrayList<>();
        if (number > 0) {
            logger.trace("creating completable future");
            for (int i = 0; i < number; i++) {
                completableFutures.add(CompletableFuture.runAsync(() -> {
                    this.addConnectionFuture(connectionSupplier);
                }));
            }
        }
        else {
            logger.info("This sort of threading failure should be rare overall, maybe?");
        }
        return completableFutures.toArray(new CompletableFuture<?>[0]);
    }

    /**
     * Drains and closes the current connection pool and marks them all loaned to be close when no longer in use, rather than returned to the pool.
     */
    public void drainAllCurrentConnections() {
        this.loanedConnections.keySet().forEach((ConnectionArtifact_I<Connection> loaned) -> loaned.getConnectionContainer().currentState = State.Empty);
        List<ConnectionArtifact_I<Connection>> drain = new ArrayList<>();
        this.connectionQueue.drainTo(drain);
        drain.forEach((ConnectionArtifact_I<Connection> artifactI) -> {
            try {
                artifactI.getDelegate().close();
            } catch (SQLException e) {
                logger.error("problem in drain" + e.getMessage());
            }
        });

    }

    /**
     * Add a connection supplier to the failover stack
     * @param failoverConnectionSupplier the new failover connection
     */
    public void addFailover(ConnectionSupplier failoverConnectionSupplier) {
        this.failoverConnectionSuppliers.offer(failoverConnectionSupplier);
    }

    public Cleanup getCleanup() {
        return this.cleanup;
    }

    public static final class DefaultProxy_withLogging extends ConnectionPool {

        public DefaultProxy_withLogging(Options options, Advice advice, BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers) {
            super(LoggingWrapperFactory.DEFAULT_INSTANCE, advice ,options,connectionSuppliers);
        }
    }
    /**
     * A connection test which will lock out the normal 'acquireConnection' method
     * @return
     */
    public boolean testAcquireConnection() {
        long writeLock = connectionGate.writeLock();
        try {
            Connection connection = activeConnectionSupplier.getConnection();
            if (connection != null) {
                return true;
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
        }
        finally {
            connectionGate.unlockWrite(writeLock);
        }
        return false;
    }

    public Options getOptions() {
        return options;
    }
}

