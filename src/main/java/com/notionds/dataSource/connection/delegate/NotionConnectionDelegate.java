package com.notionds.dataSource.connection.delegate;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.NotionConnection;

import java.sql.*;
import java.time.Instant;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.StampedLock;

public class NotionConnectionDelegate<O extends Options, DM extends DelegateMapper> extends NotionConnection<O> {

    
    public NotionConnectionDelegate(O options, DM delegateMapper, Connection delegate) {
        super(options,delegate);
    }

    public UUID getConnectionId() {
        return this.delegateMapper.getConnectionId();
    }

    @Override
    public Statement createStatement() throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().createStatement(), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().prepareStatement(sql), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().prepareCall(sql),this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().nativeSQL(sql);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().setAutoCommit(autoCommit);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().getAutoCommit();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void commit() throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().commit();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void rollback() throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().rollback();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void close() throws SQLException {
        try {
            this.notionWrapper.close();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().isClosed();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().getMetaData(), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().setReadOnly(readOnly);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().isReadOnly();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().setCatalog(catalog);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public String getCatalog() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().getCatalog();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().setTransactionIsolation(level);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().getTransactionIsolation();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().getWarnings();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().clearWarnings();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().createStatement(resultSetType, resultSetConcurrency), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().prepareStatement(sql, resultSetType, resultSetConcurrency), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().prepareCall(sql, resultSetType, resultSetConcurrency), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().getTypeMap();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().setTypeMap(map);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().setHoldability(holdability);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public int getHoldability() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().getHoldability();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().setSavepoint();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().setSavepoint(name);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().rollback(savepoint);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().releaseSavepoint(savepoint);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().prepareCall(sql, resultSetConcurrency, resultSetHoldability), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().prepareStatement(sql, autoGeneratedKeys), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().prepareStatement(sql, columnIndexes), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().prepareStatement(sql, columnNames), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public Clob createClob() throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().createClob(), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public Blob createBlob() throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().createBlob(), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public NClob createNClob() throws SQLException {
        try {
            return this.notionWrapper.wrap(notionWrapper.getUnderlyingVendorConnection().createNClob(), this);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().createSQLXML();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().isValid(timeout);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        try {
            notionWrapper.getUnderlyingVendorConnection().setClientInfo(name, value);
        }
        catch (SQLClientInfoException sqlClientInfoException) {
            throw this.notionWrapper.handleSQLClientInfoExcpetion(sqlClientInfoException, this);
        }
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        try {
            notionWrapper.getUnderlyingVendorConnection().setClientInfo(properties);
        }
        catch (SQLClientInfoException sqlClientInfoException) {
            throw this.notionWrapper.handleSQLClientInfoExcpetion(sqlClientInfoException, this);
        }
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().getClientInfo(name);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().getClientInfo();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().createArrayOf(typeName, elements);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().createStruct(typeName, attributes);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().setSchema(schema);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public String getSchema() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().getSchema();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().abort(executor);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        try {
            notionWrapper.getUnderlyingVendorConnection().setNetworkTimeout(executor, milliseconds);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().getNetworkTimeout();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().unwrap(iface);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().isWrapperFor(iface);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handle(sqlException, this);
        }
    }
}
