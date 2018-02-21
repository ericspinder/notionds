package com.notionds.dataSource;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class NotionConnection<DM extends DelegateMapper<?,?,?>> extends DelegatedInstance<DM, Connection> implements Connection {

    public NotionConnection(DM delegateMapper, Connection delegate) {
        super(delegateMapper, delegate);
    }

    @Override
    public Statement createStatement() throws SQLException {
        try {
            return delegateMapper.wrap(delegate.createStatement(), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.prepareStatement(sql), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.prepareCall(sql),this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        try {
            return delegate.nativeSQL(sql);
        }
        catch (SQLException sqlException) {
                throw delegateMapper.getExceptionHandler().handle(sqlException, this);
            }
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            try {
                delegate.setAutoCommit(autoCommit);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        try {
            return delegate.getAutoCommit();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void commit() throws SQLException {
        try {
            delegate.commit();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void rollback() throws SQLException {
        try {
            delegate.rollback();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void close() throws SQLException {
        try {
            delegateMapper.close();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        try {
            return delegate.isClosed();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getMetaData(), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        try {
            delegate.setReadOnly(readOnly);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        try {
            return delegate.isReadOnly();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        try {
            delegate.setCatalog(catalog);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public String getCatalog() throws SQLException {
        try {
            return delegate.getCatalog();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        try {
            delegate.setTransactionIsolation(level);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        try {
            return delegate.getTransactionIsolation();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        try {
            return delegate.getWarnings();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        try {
            delegate.clearWarnings();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.createStatement(resultSetType, resultSetConcurrency), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.prepareStatement(sql, resultSetType, resultSetConcurrency), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.prepareCall(sql, resultSetType, resultSetConcurrency), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        try {
            return delegate.getTypeMap();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        try {
            delegate.setTypeMap(map);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        try {
            delegate.setHoldability(holdability);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public int getHoldability() throws SQLException {
        try {
            return delegate.getHoldability();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        try {
            return delegate.setSavepoint();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        try {
            return delegate.setSavepoint(name);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        try {
            delegate.rollback(savepoint);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        try {
            delegate.releaseSavepoint(savepoint);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.prepareCall(sql, resultSetConcurrency, resultSetHoldability), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.prepareStatement(sql, autoGeneratedKeys), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.prepareStatement(sql, columnIndexes), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.prepareStatement(sql, columnNames), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public Clob createClob() throws SQLException {
        try {
            return delegateMapper.wrap(delegate.createClob(), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public Blob createBlob() throws SQLException {
        try {
            return delegateMapper.wrap(delegate.createBlob(), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public NClob createNClob() throws SQLException {
        try {
            return delegateMapper.wrap(delegate.createNClob(), this);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        try {
            return delegate.createSQLXML();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        try {
            return delegate.isValid(timeout);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        try {
            delegate.setClientInfo(name, value);
        }
        catch (SQLClientInfoException sqlClientInfoException) {
            throw delegateMapper.getExceptionHandler().handle(sqlClientInfoException, this);
        }
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        try {
            delegate.setClientInfo(properties);
        }
        catch (SQLClientInfoException sqlClientInfoException) {
            throw delegateMapper.getExceptionHandler().handle(sqlClientInfoException, this);
        }
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        try {
            return delegate.getClientInfo(name);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        try {
            return delegate.getClientInfo();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        try {
            return delegate.createArrayOf(typeName, elements);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        try {
            return delegate.createStruct(typeName, attributes);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        try {
            delegate.setSchema(schema);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public String getSchema() throws SQLException {
        try {
            return delegate.getSchema();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        try {
            delegate.abort(executor);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        try {
            delegate.setNetworkTimeout(executor, milliseconds);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        try {
            return delegate.getNetworkTimeout();
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return delegate.unwrap(iface);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        try {
            return delegate.isWrapperFor(iface);
        }
        catch (SQLException sqlException) {
            throw delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }
}
