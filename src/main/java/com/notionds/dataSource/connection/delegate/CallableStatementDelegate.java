package com.notionds.dataSource.connection.delegate;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public class CallableStatementDelegate<DM extends DelegateMapper, CS extends CallableStatement> extends PreparedStatementDelegate<DM, CS> implements CallableStatement {

    public CallableStatementDelegate(DM delegateMapper, CS delegate) {
        super(delegateMapper, delegate);
    }

    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        try {
            delegate.registerOutParameter(parameterIndex, sqlType);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        try {
            delegate.registerOutParameter(parameterIndex, sqlType, scale);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public boolean wasNull() throws SQLException {
        try {
            return delegate.wasNull();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public String getString(int parameterIndex) throws SQLException {
        try {
            return delegate.getString(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public boolean getBoolean(int parameterIndex) throws SQLException {
        try {
            return delegate.getBoolean(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public byte getByte(int parameterIndex) throws SQLException {
        try {
            return delegate.getByte(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public short getShort(int parameterIndex) throws SQLException {
        try {
            return delegate.getShort(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public int getInt(int parameterIndex) throws SQLException {
        try {
            return delegate.getInt(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public long getLong(int parameterIndex) throws SQLException {
        try {
            return delegate.getLong(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public float getFloat(int parameterIndex) throws SQLException {
        try {
            return delegate.getFloat(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public double getDouble(int parameterIndex) throws SQLException {
        try {
            return delegate.getDouble(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    @Deprecated
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        try {
            return delegate.getBigDecimal(parameterIndex, scale);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public byte[] getBytes(int parameterIndex) throws SQLException {
        try {
            return delegate.getBytes(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Date getDate(int parameterIndex) throws SQLException {
        try {
            return delegate.getDate(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Time getTime(int parameterIndex) throws SQLException {
        try {
            return delegate.getTime(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        try {
            return delegate.getTimestamp(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Object getObject(int parameterIndex) throws SQLException {
        try {
            return delegate.getObject(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        try {
            return delegate.getBigDecimal(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        try {
            return delegate.getObject(parameterIndex, map);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Ref getRef(int parameterIndex) throws SQLException {
        try {
            return delegate.getRef(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Blob getBlob(int parameterIndex) throws SQLException {
        try {
            return delegate.getBlob(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Clob getClob(int parameterIndex) throws SQLException {
        try {
            return delegate.getClob(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Array getArray(int parameterIndex) throws SQLException {
        try {
            return delegate.getArray(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        try {
            return delegate.getDate(parameterIndex, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        try {
            return delegate.getTime(parameterIndex, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        try {
            return delegate.getTimestamp(parameterIndex, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        try {
            delegate.registerOutParameter(parameterIndex, sqlType, typeName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        try {
            delegate.registerOutParameter(parameterName, sqlType);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        try {
            delegate.registerOutParameter(parameterName, sqlType, scale);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        try {
            delegate.registerOutParameter(parameterName, sqlType, typeName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public URL getURL(int parameterIndex) throws SQLException {
        try {
            return delegate.getURL(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setURL(String parameterName, URL val) throws SQLException {
        try {
            delegate.setURL(parameterName, val);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNull(String parameterName, int sqlType) throws SQLException {
        try {
            delegate.setNull(parameterName, sqlType);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBoolean(String parameterName, boolean x) throws SQLException {
        try {
            delegate.setBoolean(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setByte(String parameterName, byte x) throws SQLException {
        try {
            delegate.setByte(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setShort(String parameterName, short x) throws SQLException {
        try {
            delegate.setShort(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setInt(String parameterName, int x) throws SQLException {
        try {
            delegate.setInt(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setLong(String parameterName, long x) throws SQLException {
        try {
            delegate.setLong(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setFloat(String parameterName, float x) throws SQLException {
        try {
            delegate.setFloat(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setDouble(String parameterName, double x) throws SQLException {
        try {
            delegate.setDouble(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        try {
            delegate.setBigDecimal(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setString(String parameterName, String x) throws SQLException {
        try {
            delegate.setString(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBytes(String parameterName, byte[] x) throws SQLException {
        try {
            delegate.setBytes(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setDate(String parameterName, Date x) throws SQLException {
        try {
            delegate.setDate(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setTime(String parameterName, Time x) throws SQLException {
        try {
            delegate.setTime(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        try {
            delegate.setTimestamp(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        try {
            delegate.setAsciiStream(parameterName, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        try {
            delegate.setBinaryStream(parameterName, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        try {
            delegate.setObject(parameterName, x, targetSqlType, scale);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        try {
            delegate.setObject(parameterName, x, targetSqlType);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setObject(String parameterName, Object x) throws SQLException {
        try {
            delegate.setObject(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        try {
            delegate.setCharacterStream(parameterName, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        try {
            delegate.setDate(parameterName, x, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        try {
            delegate.setTime(parameterName, x, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        try {
            delegate.setTimestamp(parameterName, x, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        try {
            delegate.setNull(parameterName, sqlType, typeName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public String getString(String parameterName) throws SQLException {
        try {
            return delegate.getString(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public boolean getBoolean(String parameterName) throws SQLException {
        try {
            return delegate.getBoolean(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public byte getByte(String parameterName) throws SQLException {
        try {
            return delegate.getByte(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public short getShort(String parameterName) throws SQLException {
        try {
            return delegate.getShort(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public int getInt(String parameterName) throws SQLException {
        try {
            return delegate.getInt(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public long getLong(String parameterName) throws SQLException {
        try {
            return delegate.getLong(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public float getFloat(String parameterName) throws SQLException {
        try {
            return delegate.getFloat(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public double getDouble(String parameterName) throws SQLException {
        try {
            return delegate.getDouble(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public byte[] getBytes(String parameterName) throws SQLException {
        try {
            return delegate.getBytes(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Date getDate(String parameterName) throws SQLException {
        try {
            return delegate.getDate(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Time getTime(String parameterName) throws SQLException {
        try {
            return delegate.getTime(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException {
        try {
            return delegate.getTimestamp(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Object getObject(String parameterName) throws SQLException {
        try {
            return delegate.getObject(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        try {
            return delegate.getBigDecimal(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        try {
            return delegate.getObject(parameterName, map);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Ref getRef(String parameterName) throws SQLException {
        try {
            return delegate.getRef(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Blob getBlob(String parameterName) throws SQLException {
        try {
            return delegate.getBlob(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Clob getClob(String parameterName) throws SQLException {
        try {
            return delegate.getClob(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Array getArray(String parameterName) throws SQLException {
        try {
            return delegate.getArray(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        try {
            return delegate.getDate(parameterName, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        try {
            return delegate.getTime(parameterName, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        try {
            return delegate.getTimestamp(parameterName, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public URL getURL(String parameterName) throws SQLException {
        try {
            return delegate.getURL(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public RowId getRowId(int parameterIndex) throws SQLException {
        try {
            return delegate.getRowId(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public RowId getRowId(String parameterName) throws SQLException {
        try {
            return delegate.getRowId(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setRowId(String parameterName, RowId x) throws SQLException {
        try {
            delegate.setRowId(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNString(String parameterName, String value) throws SQLException {
        try {
            delegate.setNString(parameterName, value);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        try {
            delegate.setNCharacterStream(parameterName, value, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNClob(String parameterName, NClob value) throws SQLException {
        try {
            delegate.setNClob(parameterName, value);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        try {
            delegate.setClob(parameterName, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        try {
            delegate.setBlob(parameterName, inputStream, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        try {
            delegate.setNClob(parameterName, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public NClob getNClob(int parameterIndex) throws SQLException {
        try {
            return delegate.getNClob(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public NClob getNClob(String parameterName) throws SQLException {
        try {
            return delegate.getNClob(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        try {
            delegate.setSQLXML(parameterName, xmlObject);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        try {
            return delegate.getSQLXML(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public SQLXML getSQLXML(String parameterName) throws SQLException {
        try {
            return delegate.getSQLXML(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public String getNString(int parameterIndex) throws SQLException {
        try {
            return delegate.getNString(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public String getNString(String parameterName) throws SQLException {
        try {
            return delegate.getNString(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        try {
            return delegate.getNCharacterStream(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Reader getNCharacterStream(String parameterName) throws SQLException {
        try {
            return delegate.getNCharacterStream(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        try {
            return delegate.getCharacterStream(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public Reader getCharacterStream(String parameterName) throws SQLException {
        try {
            return delegate.getCharacterStream(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBlob(String parameterName, Blob x) throws SQLException {
        try {
            delegate.setBlob(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setClob(String parameterName, Clob x) throws SQLException {
        try {
            delegate.setClob(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        try {
            delegate.setAsciiStream(parameterName, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        try {
            delegate.setBinaryStream(parameterName, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        try {
            delegate.setCharacterStream(parameterName, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        try {
            delegate.setAsciiStream(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        try {
            delegate.setBinaryStream(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        try {
            delegate.setCharacterStream(parameterName, reader);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        try {
            delegate.setNCharacterStream(parameterName, value);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setClob(String parameterName, Reader reader) throws SQLException {
        try {
            delegate.setClob(parameterName, reader);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        try {
            delegate.setBlob(parameterName, inputStream);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setNClob(String parameterName, Reader reader) throws SQLException {
        try {
            delegate.setNClob(parameterName, reader);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        try {
            return delegate.getObject(parameterIndex, type);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        try {
            return delegate.getObject(parameterName, type);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setObject(String parameterName, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        try {
            delegate.setObject(parameterName, x, targetSqlType, scaleOrLength);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void setObject(String parameterName, Object x, SQLType targetSqlType) throws SQLException {
        try {
            delegate.setObject(parameterName, x, targetSqlType);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void registerOutParameter(int parameterIndex, SQLType sqlType) throws SQLException {
        try {
            delegate.registerOutParameter(parameterIndex, sqlType);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void registerOutParameter(int parameterIndex, SQLType sqlType, int scale) throws SQLException {
        try {
            delegate.registerOutParameter(parameterIndex, sqlType, scale);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void registerOutParameter(int parameterIndex, SQLType sqlType, String typeName) throws SQLException {
        try {
            delegate.registerOutParameter(parameterIndex, sqlType, typeName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void registerOutParameter(String parameterName, SQLType sqlType) throws SQLException {
        try {
            delegate.registerOutParameter(parameterName, sqlType);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void registerOutParameter(String parameterName, SQLType sqlType, int scale) throws SQLException {
        try {
            delegate.registerOutParameter(parameterName, sqlType, scale);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }

    public void registerOutParameter(String parameterName, SQLType sqlType, String typeName) throws SQLException {
        try {
            delegate.registerOutParameter(parameterName, sqlType, typeName);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.handle(sqlException, this);
        }
    }
}
