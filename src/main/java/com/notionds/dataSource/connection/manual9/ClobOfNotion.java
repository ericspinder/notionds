package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.connection.accounting.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.ConnectionContainer;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

public class ClobOfNotion implements Clob, ConnectionMember_I {

    protected final ConnectionContainer connectionContainer;
    protected final Clob delegate;
    private final OperationAccounting operationAccounting;

    public ClobOfNotion(ConnectionContainer connectionContainer, Clob delegate, OperationAccounting operationAccounting) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
        this.operationAccounting = operationAccounting;

    }

    public final OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    public final ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    public void closeDelegate() {
        this.connectionContainer.getConnectionCleanup().close(this);
    }

    @Override
    public long length() throws SQLException {
        try {
            return delegate.length();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getSubString(long pos, int length) throws SQLException {
        try {
            return delegate.getSubString(pos, length);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        try {
            return (Reader) this.connectionContainer.wrap(delegate.getCharacterStream(), Reader.class, this, null);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
        try {
            return (InputStream) connectionContainer.wrap(delegate.getAsciiStream(), InputStream.class, this, null);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public long position(String searchstr, long start) throws SQLException {
        try {
            return delegate.position(searchstr, start);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public long position(Clob searchstr, long start) throws SQLException {
        try {
            return delegate.position(searchstr, start);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
        try {
            return delegate.setString(pos, str);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        try {
            return delegate.setString(pos, str, offset, len);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public OutputStream setAsciiStream(long pos) throws SQLException {
        try {
            return (OutputStream) this.connectionContainer.wrap(delegate.setAsciiStream(pos), OutputStream.class, this, null);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Writer setCharacterStream(long pos) throws SQLException {
        try {
            return (Writer) this.connectionContainer.wrap(delegate.setCharacterStream(pos), Writer.class, this, null);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void truncate(long len) throws SQLException {
        try {
            delegate.truncate(len);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void free() throws SQLException {
        this.closeDelegate();
    }

    @Override
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        try {
            return (Reader) this.connectionContainer.wrap(delegate.getCharacterStream(pos, length), Reader.class, this, null);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }
}
