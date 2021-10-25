package com.notionds.dataSource;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.Cleanup;
import com.notionds.dataSource.connection.delegation.AbstractConnectionWrapperFactory;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ConnectionWrapperFactory;
import com.notionds.dataSource.connection.delegation.jdbcProxy.logging.ConnectionWrapperFactoryWithLogging;
import com.notionds.dataSource.exceptions.Advice;
import com.notionds.dataSource.exceptions.Recommendation;
import com.notionds.dataSource.exceptions.SqlExceptionWrapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.StampedLock;

public abstract class NotionDs<O extends Options, A extends Advice, P extends ConnectionPool, W extends AbstractConnectionWrapperFactory, C extends Cleanup<?,P>> {

    private final O options;
    private final W delegation;
    private final A advice;
    private final P connectionPool;
    private final C cleanup;
    private ConnectionSupplier_I connectionSupplier;
    private final Queue<ConnectionSupplier_I> failoverConnectionSuppliers = new ConcurrentLinkedQueue<>();
    private final Executor executor;
    private final StampedLock connectionGate = new StampedLock();
    private final StampedLock newConnectionGate = new StampedLock();

    public interface AdviceSupplier_I {
        Recommendation reviewRecommendation();
    }
    public interface ConnectionSupplier_I {
        Connection getConnection() throws SQLException;
    }
    private static StampedLock gate = new StampedLock();

    public static final class Default extends NotionDs<Options.Default, Advice.Default<?>, ConnectionPool.Default, ConnectionWrapperFactory<?>, Cleanup.Default> {

        public Default(ConnectionSupplier_I connectionSupplier) {
            super(Options.DEFAULT_OPTIONS_INSTANCE, ConnectionWrapperFactory.DEFAULT_INSTANCE, ConnectionPool.DEFAULT_INSTANCE, new Advice.Default<>(connectionSupplier), Cleanup.DEFAULT_INSTANCE, connectionSupplier, new ForkJoinPool(10));
        }
    }
    public static final class Default_withLogging extends NotionDs<Options.Default, Advice.Default<?>, ConnectionPool.Default, ConnectionWrapperFactoryWithLogging<?,?,?,?>, Cleanup.Default> {

        public Default_withLogging(ConnectionSupplier_I connectionSupplier) {
            super(Options.DEFAULT_OPTIONS_INSTANCE, ConnectionWrapperFactoryWithLogging.DEFAULT_INSTANCE, ConnectionPool.DEFAULT_INSTANCE, new Advice.Default<>(connectionSupplier), Cleanup.DEFAULT_INSTANCE, connectionSupplier, new ForkJoinPool(10));
        }
    }

    public NotionDs(O options, W delegation, P connectionPool, A advice, C cleanup, ConnectionSupplier_I connectionSupplier, Executor executor) {
        long lock = gate.writeLock();
        try {
            this.options = options;
            this.delegation = delegation;
            this.connectionPool = connectionPool;
            this.advice = advice;
            this.cleanup = cleanup;
            this.connectionSupplier = connectionSupplier;
            this.executor = executor;
        }
        finally {
            gate.unlockWrite(lock);
        }
    }

    /**
     * Add a connection supplier to the failover stack
     * @param failoverConnectionSupplier
     */
    public void addFailover(ConnectionSupplier_I failoverConnectionSupplier) {
        this.failoverConnectionSuppliers.offer(failoverConnectionSupplier);
    }

    /**
     * Updates the active connection supplier
     * @param burn clears connections which may still be active from previous connection supplier
     */
    public void doFailover(Boolean burn) {
        long newConnectionLock = newConnectionGate.writeLock();
        try {
            ConnectionSupplier_I failoverConnection = this.failoverConnectionSuppliers.poll();
            if (failoverConnection != null) {
                this.connectionSupplier = failoverConnection;
                if (burn) {
                    this.connectionPool.drainAllCurrentConnections();
                }
            }
            else {
                throw new NotionStartupException(NotionStartupException.Type.No_Failover_Available, this.getClass());
            }
        }
        finally {
            newConnectionGate.unlockWrite(newConnectionLock);
        }
    }

    /**
     * A connection test which will lock out the normal 'acquireConnection' method
     * @return
     */
    public Connection testConnectionBeforeStart() {
        long writeLock = connectionGate.writeLock();
        ConnectionArtifact_I connection = newPopulatedConnectionContainer();
        if (connection != null) {
            connectionGate.unlockWrite(writeLock);
            return (Connection) connection;
        }
        throw new NotionStartupException(NotionStartupException.Type.TEST_CONNECTION_FAILURE, NotionDs.class);
    }

    @SuppressWarnings("unchecked")
    public Connection acquireConnection(Duration duration) throws SqlExceptionWrapper {
        long readLock = connectionGate.readLock();
        try {
            ConnectionArtifact_I wrapped = connectionPool.getConnection(this::newPopulatedConnectionContainer);
            wrapped.getConnectionMain().checkoutFromPool(duration);
            return (Connection) wrapped.getConnectionMain().getConnection();
        }
        finally {
            connectionGate.unlockRead(readLock);
        }
    }

    protected ConnectionArtifact_I newPopulatedConnectionContainer() {
        ConnectionContainer<O, A, W, C> connectionContainer = new ConnectionContainer<>(this.options, this.advice, this.delegation, this.cleanup);
        long newConnectionLock = newConnectionGate.readLock();
        try {
            return connectionContainer.wrap(this.connectionSupplier.getConnection(), Connection.class, null);
        } catch (SQLException e) {
            e.printStackTrace();
            connectionContainer.handleSQLException(e, null);
            return null;
        }
        finally {
            newConnectionGate.unlockRead(newConnectionLock);
        }
    }

    public CompletableFuture<ConnectionArtifact_I> futureConnectionToPool() {
        CompletableFuture<ConnectionArtifact_I> future = new CompletableFuture<>();
        future.complete(this.newPopulatedConnectionContainer());
        return future;
    }
    public P getConnectionPool() {
        return connectionPool;
    }

    public C getCleanup() {
        return cleanup;
    }
}

