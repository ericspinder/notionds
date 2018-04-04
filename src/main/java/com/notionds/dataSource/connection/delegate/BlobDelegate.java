package com.notionds.dataSource.connection.delegate;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobDelegate<DM extends DelegateMapper> extends ConnectionMember<DM, AutoCloseableBlob> implements Blob {

    public BlobDelegate(DM delegatedMapper, AutoCloseableBlob autoCloseableBlob) {
        super(delegatedMapper, autoCloseableBlob);
    }

    public long length() throws SQLException {
        try {
            return delegate.length();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public byte[] getBytes(long pos, int length) throws SQLException {
        try {
            return delegate.getBytes(pos, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public InputStream getBinaryStream() throws SQLException {
        try {
            return delegate.getBinaryStream();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public long position(byte[] pattern, long start) throws SQLException {
        try {
            return delegate.position(pattern, start);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public long position(Blob pattern, long start) throws SQLException {
        try {
            return delegate.position(pattern, start);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public int setBytes(long pos, byte[] bytes) throws SQLException {
        try {
            return delegate.setBytes(pos, bytes);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        try {
            return delegate.setBytes(pos, bytes, offset, len);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public OutputStream setBinaryStream(long pos) throws SQLException {
        try {
            return delegate.setBinaryStream(pos);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void truncate(long len) throws SQLException {
        try {
            delegate.truncate(len);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void free() throws SQLException {
        try {
            delegate.free();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        try {
            return delegate.getBinaryStream(pos, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }
}
