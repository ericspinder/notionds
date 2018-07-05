package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.generator.ConnectionContainer;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class NotionConnectionDelegate implements Connection, ConnectionMember_I {

    protected final ConnectionContainer connectionContainer;
    private final OperationAccounting operationAccounting;
    private State state = State.Open;
    private final Connection delegate;

    public NotionConnectionDelegate(ConnectionContainer connectionContainer, Connection delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
        this.operationAccounting = new OperationAccounting(connectionContainer.getConnectionId());
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    @Override
    public final void close() throws SQLException {
        this.connectionContainer.close(this);
    }


    @Override
    public final boolean isClosed() throws SQLException {
        return State.Closed.equals(this.getState());
    }


    @Override
    public Statement createStatement() throws SQLException {
        try {
            return (Statement) this.connectionContainer.wrap(delegate.createStatement(), Statement.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        try {
            return (PreparedStatement) this.connectionContainer.wrap(delegate.prepareStatement(sql), PreparedStatement.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        try {
            return (CallableStatement) this.connectionContainer.wrap(delegate.prepareCall(sql),CallableStatement.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        try {
            return delegate.nativeSQL(sql);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        try {
            delegate.setAutoCommit(autoCommit);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        try {
            return delegate.getAutoCommit();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void commit() throws SQLException {
        try {
            delegate.commit();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void rollback() throws SQLException {
        try {
            delegate.rollback();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        try {
            return (DatabaseMetaData) this.connectionContainer.wrap(delegate.getMetaData(), DatabaseMetaData.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        try {
            delegate.setReadOnly(readOnly);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        try {
            return delegate.isReadOnly();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        try {
            delegate.setCatalog(catalog);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getCatalog() throws SQLException {
        try {
            return delegate.getCatalog();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        try {
            delegate.setTransactionIsolation(level);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        try {
            return delegate.getTransactionIsolation();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        try {
            return delegate.getWarnings();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        try {
            delegate.clearWarnings();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return (Statement) this.connectionContainer.wrap(delegate.createStatement(resultSetType, resultSetConcurrency), Statement.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return (PreparedStatement) this.connectionContainer.wrap(delegate.prepareStatement(sql, resultSetType, resultSetConcurrency), PreparedStatement.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return (CallableStatement) this.connectionContainer.wrap(delegate.prepareCall(sql, resultSetType, resultSetConcurrency), CallableStatement.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        try {
            return delegate.getTypeMap();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        try {
            delegate.setTypeMap(map);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        try {
            delegate.setHoldability(holdability);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getHoldability() throws SQLException {
        try {
            return delegate.getHoldability();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        try {
            return delegate.setSavepoint();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        try {
            return delegate.setSavepoint(name);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        try {
            delegate.rollback(savepoint);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        try {
            delegate.releaseSavepoint(savepoint);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return (Statement) this.connectionContainer.wrap(delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability), Statement.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return (PreparedStatement) this.connectionContainer.wrap(delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), PreparedStatement.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return (CallableStatement) this.connectionContainer.wrap(delegate.prepareCall(sql, resultSetConcurrency, resultSetHoldability), CallableStatement.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        try {
            return (PreparedStatement) this.connectionContainer.wrap(delegate.prepareStatement(sql, autoGeneratedKeys), PreparedStatement.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        try {
            return (PreparedStatement) this.connectionContainer.wrap(delegate.prepareStatement(sql, columnIndexes), PreparedStatement.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        try {
            return (PreparedStatement) this.connectionContainer.wrap(delegate.prepareStatement(sql, columnNames), PreparedStatement.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Clob createClob() throws SQLException {
        try {
            return (Clob) this.connectionContainer.wrap(delegate.createClob(), Clob.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Blob createBlob() throws SQLException {
        try {
            return (Blob) this.connectionContainer.wrap(delegate.createBlob(), Blob.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public NClob createNClob() throws SQLException {
        try {
            return (NClob) this.connectionContainer.wrap(delegate.createNClob(), NClob.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        try {
            return (SQLXML) this.connectionContainer.wrap(delegate.createSQLXML(), SQLXML.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        try {
            return delegate.isValid(timeout);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        try {
            delegate.setClientInfo(name, value);
        }
        catch (SQLClientInfoException sqlClientInfoException) {
            throw this.connectionContainer.handleSQLClientInfoExcpetion(sqlClientInfoException, this);
        }
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        try {
            delegate.setClientInfo(properties);
        }
        catch (SQLClientInfoException sqlClientInfoException) {
            throw this.connectionContainer.handleSQLClientInfoExcpetion(sqlClientInfoException, this);
        }
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        try {
            return delegate.getClientInfo(name);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        try {
            return delegate.getClientInfo();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        try {
            return (Array) this.connectionContainer.wrap(delegate.createArrayOf(typeName, elements), Array.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        try {
            return (Struct) this.connectionContainer.wrap(delegate.createStruct(typeName, attributes), Struct.class);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        try {
            delegate.setSchema(schema);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getSchema() throws SQLException {
        try {
            return delegate.getSchema();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        try {
            delegate.abort(executor);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        try {
            delegate.setNetworkTimeout(executor, milliseconds);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        try {
            return delegate.getNetworkTimeout();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return delegate.unwrap(iface);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        try {
            return delegate.isWrapperFor(iface);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }
}
