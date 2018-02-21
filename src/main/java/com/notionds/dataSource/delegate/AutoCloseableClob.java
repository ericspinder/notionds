package com.notionds.dataSource.delegate;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

public class AutoCloseableClob implements AutoCloseable, Clob {

    private final Clob delegate;

    public AutoCloseableClob(Clob delegate) {
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
    public String getSubString(long pos, int length) throws SQLException {
        return delegate.getSubString(pos, length);
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        return delegate.getCharacterStream();
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
        return delegate.getAsciiStream();
    }

    @Override
    public long position(String searchstr, long start) throws SQLException {
        return delegate.position(searchstr, start);
    }

    @Override
    public long position(Clob searchstr, long start) throws SQLException {
        return delegate.position(searchstr, start);
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
        return delegate.setString(pos, str);
    }

    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        return delegate.setString(pos, str, offset, len);
    }

    @Override
    public OutputStream setAsciiStream(long pos) throws SQLException {
        return delegate.setAsciiStream(pos);
    }

    @Override
    public Writer setCharacterStream(long pos) throws SQLException {
        return delegate.setCharacterStream(pos);
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
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        return delegate.getCharacterStream(pos, length);
    }
}
