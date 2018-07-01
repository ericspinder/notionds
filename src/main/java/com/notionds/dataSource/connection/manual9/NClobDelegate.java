package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.connection.generator.ClobOfNotion;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLException;

public class NClobDelegate extends ClobOfNotion<ConnectionContainerManual9> implements NClob {

    public NClobDelegate(ConnectionContainerManual9 notionWrapper, NClob delegate) {
        super(notionWrapper, delegate);
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
    public String getSubString(long pos, int length) throws SQLException {
        try {
            return delegate.getSubString(pos, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        try {
            return delegate.getCharacterStream();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
        try {
            return delegate.getAsciiStream();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public long position(String searchstr, long start) throws SQLException {
        try {
            return delegate.position(searchstr, start);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public long position(Clob searchstr, long start) throws SQLException {
        try {
            return delegate.position(searchstr, start);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
        try {
            return delegate.setString(pos, str);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        try {
            return delegate.setString(pos, str, offset, len);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public OutputStream setAsciiStream(long pos) throws SQLException {
        try {
            return delegate.setAsciiStream(pos);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Writer setCharacterStream(long pos) throws SQLException {
        try {
            return delegate.setCharacterStream(pos);
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
        this.notionWrapper.close(this);
    }

    @Override
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        try {
            return delegate.getCharacterStream(pos, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }
}
