package com.notionds.dataSource.connection.v1;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class AutoCloseableBlob<BB extends Blob> implements AutoCloseable, Blob {

    private final BB delegate;

    public AutoCloseableBlob(BB delegate) {
        this.delegate = delegate;
    }

    @Override
    public void close() throws SQLException {
        delegate.free();
    }

    @Override
    public long length() throws SQLException {
        return delegate.length();
    }

    @Override
    public byte[] getBytes(long pos, int length) throws SQLException {
        return delegate.getBytes(pos, length);
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        return delegate.getBinaryStream();
    }

    @Override
    public long position(byte[] pattern, long start) throws SQLException {
        return delegate.position(pattern, start);
    }

    @Override
    public long position(Blob pattern, long start) throws SQLException {
        return delegate.position(pattern, start);
    }

    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        return delegate.setBytes(pos, bytes);
    }

    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        return delegate.setBytes(pos, bytes, offset, len);
    }

    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {
        return delegate.setBinaryStream(pos);
    }

    @Override
    public void truncate(long len) throws SQLException {
        delegate.truncate(len);
    }

    @Override
    public void free() throws SQLException {
        delegate.free();
    }

    @Override
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        return delegate.getBinaryStream(pos, length);
    }
}
