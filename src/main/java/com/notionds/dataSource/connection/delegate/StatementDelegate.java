package com.notionds.dataSource.connection.delegate;

import java.sql.*;

public class StatementDelegate<DM extends DelegateMapper, S extends Statement> extends ConnectionMember<DM, S> implements Statement {

    public StatementDelegate(DM delegateMapper, S delegate) {
        super(delegateMapper, delegate);
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.executeQuery(sql), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        try {
            return delegate.executeUpdate(sql);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void close() throws SQLException {
        try {
            delegateMapper.close();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        try {
            return delegate.getMaxFieldSize();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        try {
            delegate.setMaxFieldSize(max);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int getMaxRows() throws SQLException {
        try {
            return delegate.getMaxRows();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        try {
            delegate.setMaxRows(max);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        try {
            delegate.setEscapeProcessing(enable);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        try {
            return delegate.getQueryTimeout();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        try {
            delegate.setQueryTimeout(seconds);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void cancel() throws SQLException {
        try {
            delegate.cancel();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        try {
            return delegate.getWarnings();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        try {
            delegate.clearWarnings();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        try {
            delegate.setCursorName(name);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        try {
            return delegate.execute(sql);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        try {
            return delegate.getResultSet();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int getUpdateCount() throws SQLException {
        try {
            return delegate.getUpdateCount();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        try {
            return delegate.getMoreResults();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        try {
            delegate.setFetchDirection(direction);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int getFetchDirection() throws SQLException {
        try {
            return delegate.getFetchDirection();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        try {
            delegate.setFetchSize(rows);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int getFetchSize() throws SQLException {
        try {
            return delegate.getFetchSize();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        try {
            return delegate.getResultSetConcurrency();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int getResultSetType() throws SQLException {
        try {
            return delegate.getResultSetType();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        try {
            delegate.addBatch(sql);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void clearBatch() throws SQLException {
        try {
            delegate.clearBatch();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int[] executeBatch() throws SQLException {
        try {
            return delegate.executeBatch();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            return delegateMapper.retrieve(delegate.getConnection(), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        try {
            return delegate.getMoreResults(current);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        try {
            return delegate.getGeneratedKeys();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        try {
            return delegate.executeUpdate(sql, autoGeneratedKeys);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        try {
            return delegate.executeUpdate(sql, columnIndexes);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        try {
            return delegate.executeUpdate(sql, columnNames);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        try {
            return delegate.execute(sql, autoGeneratedKeys);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        try {
            return delegate.execute(sql, columnIndexes);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        try {
            return delegate.execute(sql, columnNames);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        try {
            return delegate.getResultSetHoldability();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        try {
            return delegate.isClosed();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        try {
            delegate.setPoolable(poolable);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean isPoolable() throws SQLException {
        try {
            return delegate.isPoolable();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        try {
            delegate.closeOnCompletion();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        try {
            return delegate.isCloseOnCompletion();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public long getLargeUpdateCount() throws SQLException {
        try {
            return delegate.getLargeUpdateCount();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public void setLargeMaxRows(long max) throws SQLException {
        try {
            delegate.setLargeMaxRows(max);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public long getLargeMaxRows() throws SQLException {
        try {
            return delegate.getLargeMaxRows();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public long[] executeLargeBatch() throws SQLException {
        try {
            return delegate.executeLargeBatch();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public long executeLargeUpdate(String sql) throws SQLException {
        try {
            return delegate.executeLargeUpdate(sql);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        try {
            return delegate.executeLargeUpdate(sql, autoGeneratedKeys);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
        try {
            return delegate.executeLargeUpdate(sql, columnIndexes);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
        try {
            return delegate.executeLargeUpdate(sql, columnNames);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public String enquoteLiteral(String val) throws SQLException {
        try {
            return delegate.enquoteLiteral(val);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public String enquoteIdentifier(String identifier, boolean alwaysQuote) throws SQLException {
        try {
            return delegate.enquoteIdentifier(identifier, alwaysQuote);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean isSimpleIdentifier(String identifier) throws SQLException {
        try {
            return delegate.isSimpleIdentifier(identifier);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public String enquoteNCharLiteral(String val) throws SQLException {
        try {
            return delegate.enquoteNCharLiteral(val);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return delegate.unwrap(iface);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        try {
            return delegate.isWrapperFor(iface);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }
}
