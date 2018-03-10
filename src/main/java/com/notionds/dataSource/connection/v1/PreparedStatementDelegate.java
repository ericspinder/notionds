package com.notionds.dataSource.connection.v1;

import com.notionds.dataSource.connection.DelegateMapper;
import com.notionds.dataSource.connection.DelegatedInstance;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public class PreparedStatementDelegate<DM extends DelegateMapper<?,?,?>, PS extends PreparedStatement> extends StatementDelegate<DM, PS> implements PreparedStatement {

    public PreparedStatementDelegate(DM delegateMapper, PS delegate) {
        super(delegateMapper, delegate);
    }

    public ResultSet executeQuery() throws SQLException {
        try {
            return delegate.executeQuery();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public int executeUpdate() throws SQLException {
        try {
            return delegate.executeUpdate();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        try {
            delegate.setNull(parameterIndex, sqlType);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        try {
            delegate.setBoolean(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        try {
            delegate.setByte(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        try {
            delegate.setShort(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        try {
            delegate.setInt(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        try {
            delegate.setLong(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        try {
            delegate.setFloat(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        try {
            delegate.setDouble(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        try {
            delegate.setBigDecimal(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        try {
            delegate.setString(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        try {
            delegate.setBytes(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        try {
            delegate.setDate(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        try {
            delegate.setTime(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        try {
            delegate.setTimestamp(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        try {
            delegate.setAsciiStream(parameterIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Deprecated(since = "1.2")
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        try {
            delegate.setUnicodeStream(parameterIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        try {
            delegate.setBinaryStream(parameterIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void clearParameters() throws SQLException {
        try {
            delegate.clearParameters();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        try {
            delegate.setObject(parameterIndex, x, targetSqlType);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setObject(int parameterIndex, Object x) throws SQLException {
        try {
            delegate.setObject(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public boolean execute() throws SQLException {
        try {
            return delegate.execute();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void addBatch() throws SQLException {
        try {
            delegate.addBatch();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        try {
            delegate.setCharacterStream(parameterIndex, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setRef(int parameterIndex, Ref x) throws SQLException {
        try {
            delegate.setRef(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        try {
            delegate.setBlob(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setClob(int parameterIndex, Clob x) throws SQLException {
        try {
            delegate.setClob(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setArray(int parameterIndex, Array x) throws SQLException {
        try {
            delegate.setArray(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        try {
            return delegate.getMetaData();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        try {
            delegate.setDate(parameterIndex, x, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        try {
            delegate.setTime(parameterIndex, x, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        try {
            delegate.setTimestamp(parameterIndex, x, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        try {
            delegate.setNull(parameterIndex, sqlType, typeName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        try {
            delegate.setURL(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        try {
            return delegate.getParameterMetaData();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        try {
            delegate.setRowId(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNString(int parameterIndex, String value) throws SQLException {
        try {
            delegate.setNString(parameterIndex, value);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        try {
            delegate.setNCharacterStream(parameterIndex, value, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        try {
            delegate.setNClob(parameterIndex, value);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        try {
            delegate.setClob(parameterIndex, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        try {
            delegate.setBlob(parameterIndex, inputStream, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        try {
            delegate.setNClob(parameterIndex, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        try {
            delegate.setSQLXML(parameterIndex, xmlObject);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        try {
            delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        try {
            delegate.setAsciiStream(parameterIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        try {
            delegate.setBinaryStream(parameterIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        try {
            delegate.setCharacterStream(parameterIndex, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        try {
            delegate.setAsciiStream(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        try {
            delegate.setBinaryStream(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        try {
            delegate.setCharacterStream(parameterIndex, reader);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        try {
            delegate.setNCharacterStream(parameterIndex, value);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        try {
            delegate.setClob(parameterIndex, reader);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        try {
            delegate.setBlob(parameterIndex, inputStream);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        try {
            delegate.setNClob(parameterIndex, reader);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        try {
            delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
        try {
            delegate.setObject(parameterIndex, x, targetSqlType);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public long executeLargeUpdate() throws SQLException {
        try {
            return delegate.executeLargeUpdate();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }
}
