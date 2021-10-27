package com.notionds.dataSource;

import com.notionds.dataSource.connection.Container;
import com.notionds.dataSource.connection.delegation.AbstractConnectionWrapperFactory;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ConnectionWrapperFactory;
import com.notionds.dataSource.connection.delegation.jdbcProxy.logging.ConnectionWrapperFactoryWithLogging;
import com.notionds.dataSource.exceptions.Advice;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.StampedLock;
import java.util.logging.Logger;

public abstract class NotionDs<O extends Options, A extends Advice, P extends ConnectionPool, W extends AbstractConnectionWrapperFactory> implements DataSource {

    private final O options;
    private final W delegation;
    private final A advice;
    private final P connectionPool;
    private final StampedLock connectionGate = new StampedLock();


    public interface ConnectionSupplier_I {
        Connection getConnection() throws SQLException;
    }

    public static final class Default extends NotionDs<Options.Default, Advice.Default_H2<?>, ConnectionPool.Default, ConnectionWrapperFactory<?>> {

        public Default(Queue<ConnectionSupplier_I> connectionSuppliers) {
            super(Options.DEFAULT_OPTIONS_INSTANCE, ConnectionWrapperFactory.DEFAULT_INSTANCE, new ConnectionPool.Default(new ForkJoinPool(10), connectionSuppliers), new Advice.Default_H2<>());
        }
    }
    public static final class Default_withLogging extends NotionDs<Options.Default, Advice.Default_H2<?>, ConnectionPool.Default, ConnectionWrapperFactoryWithLogging<?,?,?,?>> {

        public Default_withLogging(Queue<ConnectionSupplier_I> connectionSuppliers) {
            super(Options.DEFAULT_OPTIONS_INSTANCE, ConnectionWrapperFactoryWithLogging.DEFAULT_INSTANCE, new ConnectionPool.Default(new ForkJoinPool(10), connectionSuppliers), new Advice.Default_H2<>());
        }
    }

    public NotionDs(O options, W delegation, P connectionPool, A advice) {
        this.options = options;
        this.delegation = delegation;
        this.connectionPool = connectionPool;
        this.advice = advice;
    }

    /**
     * A connection test which will lock out the normal 'acquireConnection' method
     * @return
     */
    public Connection testConnection() {
        long writeLock = connectionGate.writeLock();
        try {
            ConnectionArtifact_I connection = newConnectionContainer();
            if (connection != null) {
                return (Connection) connection;
            }
            throw new NotionStartupException(NotionStartupException.Type.TEST_CONNECTION_FAILURE, NotionDs.class);
        }
        finally {
            connectionGate.unlockWrite(writeLock);
        }
    }

    /**
     * The
     * @param duration
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public Connection getConnection(Duration duration) throws SQLException {
        long readLock = connectionGate.readLock();
        try {
            ConnectionArtifact_I wrapped = connectionPool.getConnection(this::newConnectionContainer);
            wrapped.getContainer().checkoutFromPool(duration);
            return (Connection) wrapped.getContainer().getConnection();
        }
        finally {
            connectionGate.unlockRead(readLock);
        }
    }

    @SuppressWarnings("unchecked")
    private ConnectionArtifact_I newConnectionContainer() {
        return this.connectionPool.populateConnectionContainer(new Container<>(this.options, this.advice, this.delegation, this.connectionPool.getCleanup()));
    }

    public P getConnectionPool() {
        return connectionPool;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.getConnection(null);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new SQLFeatureNotSupportedException("getConnection(String username, String password) is unsupported");
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new SQLFeatureNotSupportedException("setLogWriter(PrintWriter out) is unsupported");
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.connectionPool.setConnection_retrieveTimeout(Duration.ofSeconds(seconds));
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return Math.toIntExact(this.connectionPool.getConnection_retrieveTimeout().get(ChronoUnit.SECONDS));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(this.getClass())) {
            return (T) this;
        }
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("getParentLogger() is unsupported");
    }
}

