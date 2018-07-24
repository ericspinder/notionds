package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.connection.accounting.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.ConnectionContainer;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobOfNotion<NW extends ConnectionContainer> implements Blob, ConnectionMember_I {

    protected final NW connectionContainer;
    protected final Blob delegate;
    private final OperationAccounting operationAccounting;

    public BlobOfNotion(NW connectionContainer, Blob delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
        this.operationAccounting = new OperationAccounting(connectionContainer.getConnectionId());
    }

    public final OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    public final ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    @Override
    public final void closeDelegate() {
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
    public byte[] getBytes(long pos, int length) throws SQLException {
        try {
            return delegate.getBytes(pos, length);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        try {
            return delegate.getBinaryStream();
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public long position(byte[] pattern, long start) throws SQLException {
        try {
            return delegate.position(pattern, start);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public long position(Blob pattern, long start) throws SQLException {
        try {
            return delegate.position(pattern, start);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        try {
            return delegate.setBytes(pos, bytes);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        try {
            return delegate.setBytes(pos, bytes, offset, len);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }

    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {
        try {
            return (OutputStream) this.connectionContainer.wrap(delegate.setBinaryStream(pos), OutputStream.class, this, null);
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
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        try {
            return delegate.getBinaryStream(pos, length);
        }
        catch (SQLException sqlException) {
            throw this.connectionContainer.handleSQLException(sqlException, this);
        }
    }
}
