package com.notionds.dataSource.delegate;

import com.notionds.dataSource.DelegateMapper;
import com.notionds.dataSource.DelegatedInstance;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

public class ClobDelegate<DM extends DelegateMapper<?,?,?>> extends DelegatedInstance<DM, AutoCloseableClob> implements Clob {

    public ClobDelegate(DM delegatedMapper, AutoCloseableClob autoCloseableClob) {
        super(delegatedMapper, autoCloseableClob);
    }

    public long length() throws SQLException {
        try {
            return delegate.length();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public String getSubString(long pos, int length) throws SQLException {
        try {
            return delegate.getSubString(pos, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Reader getCharacterStream() throws SQLException {
        try {
            return delegate.getCharacterStream();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public InputStream getAsciiStream() throws SQLException {
        try {
            return delegate.getAsciiStream();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public long position(String searchstr, long start) throws SQLException {
        try {
            return delegate.position(searchstr, start);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public long position(Clob searchstr, long start) throws SQLException {
        try {
            return delegate.position(searchstr, start);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public int setString(long pos, String str) throws SQLException {
        try {
            return delegate.setString(pos, str);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public int setString(long pos, String str, int offset, int len) throws SQLException {
        try {
            return delegate.setString(pos, str, offset, len);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public OutputStream setAsciiStream(long pos) throws SQLException {
        try {
            return delegate.setAsciiStream(pos);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Writer setCharacterStream(long pos) throws SQLException {
        try {
            return delegate.setCharacterStream(pos);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void truncate(long len) throws SQLException {
        try {
            delegate.truncate(len);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void free() throws SQLException {
        try {
            delegate.free();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Reader getCharacterStream(long pos, long length) throws SQLException {
        try {
            return delegate.getCharacterStream(pos, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }
}
