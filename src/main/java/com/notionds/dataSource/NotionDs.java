package com.notionds.dataSource;



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
        String getTestSQL();
    }

    public static final Options.Default DEFAULT_OPTIONS_INSTANCE = new Options.Default();
    private final ConnectionPool connectionPool;


    public NotionDs(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        if (this.connectionPool.warmPool()) {
            Thread cleaningThread = new Thread(this.connectionPool.getCleanupPrepare());
            cleaningThread.setName("CleanPrepare " + UUID.randomUUID());
            cleaningThread.start();
            Runtime.getRuntime().addShutdownHook(new Thread(connectionPool::shutdown));
        }
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
    public PrintWriter getLogWriter() {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new SQLFeatureNotSupportedException("setLogWriter(PrintWriter out) is unsupported");
    }

    /**
     * Note that this is in milliseconds rather than Seconds as described by the API
     * @param milliseconds the data source login time limit
     */
    @Override
    public void setLoginTimeout(int milliseconds) {
        this.connectionPool.getOptions().setValue(Options.Integers.Timeout_Retrieve_Connection.getKey(), milliseconds);
    }

    @Override
    public int getLoginTimeout() {
        return (int) this.connectionPool.getOptions().get(Options.Integers.Timeout_Retrieve_Connection.getKey())/1000;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) {
        if (iface.isAssignableFrom(this.getClass())) {
            return (T) this;
        }
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("getParentLogger() is unsupported");
    }

}

