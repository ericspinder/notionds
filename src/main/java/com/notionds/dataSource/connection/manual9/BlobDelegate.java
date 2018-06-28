package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.connection.ConnectionMember_I;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

public class BlobDelegate<DM extends NotionWrapperManual9> implements Blob, ConnectionMember_I {

    private final DM notionWrapper;
    private final Blob delegate;
    private final Instant createInstant = Instant.now();

    public BlobDelegate(DM delegatedMapper, Blob delegate) {
        this.notionWrapper = delegatedMapper;
        this.delegate = delegate;
    }
    public final UUID getConnectionId() {
        return this.notionWrapper.getConnectionId();
    }
    public final Instant getCreateInstant() {
        return this.createInstant;
    }

    @Override
    public void close() throws SQLException {
        this.notionWrapper.close(this);
    }
    public void closeDelegate() throws SQLException {
        this.delegate.free();
    }

    @Override
    public long length() throws SQLException {
        try {
            return delegate.length();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public byte[] getBytes(long pos, int length) throws SQLException {
        try {
            return delegate.getBytes(pos, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        try {
            return delegate.getBinaryStream();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public long position(byte[] pattern, long start) throws SQLException {
        try {
            return delegate.position(pattern, start);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public long position(Blob pattern, long start) throws SQLException {
        try {
            return delegate.position(pattern, start);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        try {
            return delegate.setBytes(pos, bytes);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        try {
            return delegate.setBytes(pos, bytes, offset, len);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {
        try {
            return delegate.setBinaryStream(pos);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void truncate(long len) throws SQLException {
        try {
            delegate.truncate(len);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public void free() throws SQLException {
        try {
            delegate.free();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        try {
            return delegate.getBinaryStream(pos, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }
}
