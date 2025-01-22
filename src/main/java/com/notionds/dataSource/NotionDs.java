package com.notionds.dataSource;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.UUID;

public class NotionDs implements DataSource {

    public interface ConnectionSupplier_I {

        UUID getUUID();
        Connection getConnection() throws SQLException;
    }

    public static final Options.Default DEFAULT_OPTIONS_INSTANCE = new Options.Default();
    private final ConnectionPool connectionPool;


    public NotionDs(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        Thread cleaningThread = new Thread(this.connectionPool.getCleanup());
        cleaningThread.start();
    }


    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.connectionPool.getConnection();
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
        this.connectionPool.getOptions().setValue(Options.Integers.Timeout_Retrieve_Connection.getKey(), seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return (int) this.connectionPool.getOptions().get(Options.Integers.Timeout_Retrieve_Connection.getKey());
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
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("getParentLogger() is unsupported");
    }

}

