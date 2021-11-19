package com.notionds.dataSupplier;

import com.notionds.dataSupplier.operational.IntegerOption;
import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.delegation.Wrapper;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.security.Security;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

public abstract class NotionDs<O extends Operational> implements DataSource {

    private static final String TTL_PROP = "networkaddress.cache.ttl";
    private final O options;


    private final Integer dnsTimeout;

    public NotionDs(O options) {
        this.options = options;
        this.dnsTimeout = options.getInteger(IntegerOption.DnsTimeout.getI18n());
        String ttlString = Security.getProperty(TTL_PROP);
        if (ttlString == null || Integer.parseInt(ttlString) < dnsTimeout) {
            java.security.Security.setProperty(TTL_PROP, dnsTimeout.toString());
        }
    }

    /**
     * A connection test which will lock out the normal 'acquireConnection' method
     * @return
     */
    public Connection testConnection() {
        long writeLock = connectionGate.writeLock();
        try {
            Wrapper connection = newConnectionContainer();
            if (connection != null) {
                return (Connection) connection;
            }
            throw new NotionStartupException(NotionStartupException.Type.TEST_CONNECTION_FAILURE, NotionDs.class);
        }
        finally {
            connectionGate.unlockWrite(writeLock);
        }
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

