package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.connection.NotionWrapper;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public class CallableStatementDelegate extends PreparedStatementDelegate implements CallableStatement {

    public CallableStatementDelegate(NotionWrapperManual9 notionWrapper, CallableStatement delegate) {
        super(notionWrapper, delegate);
    }
    
    private CallableStatement getCallableStatement() {
        return (CallableStatement)delegate;
    }

    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        try {
            getCallableStatement().registerOutParameter(parameterIndex, sqlType);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        try {
            getCallableStatement().registerOutParameter(parameterIndex, sqlType, scale);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public boolean wasNull() throws SQLException {
        try {
            return getCallableStatement().wasNull();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public String getString(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getString(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public boolean getBoolean(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getBoolean(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public byte getByte(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getByte(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public short getShort(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getShort(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public int getInt(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getInt(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public long getLong(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getLong(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public float getFloat(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getFloat(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public double getDouble(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getDouble(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Deprecated
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        try {
            return getCallableStatement().getBigDecimal(parameterIndex, scale);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public byte[] getBytes(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getBytes(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Date getDate(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getDate(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Time getTime(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getTime(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getTimestamp(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Object getObject(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getObject(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getBigDecimal(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        try {
            return getCallableStatement().getObject(parameterIndex, map);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Ref getRef(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getRef(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Blob getBlob(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getBlob(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Clob getClob(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getClob(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Array getArray(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getArray(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        try {
            return getCallableStatement().getDate(parameterIndex, cal);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        try {
            return getCallableStatement().getTime(parameterIndex, cal);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        try {
            return getCallableStatement().getTimestamp(parameterIndex, cal);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        try {
            getCallableStatement().registerOutParameter(parameterIndex, sqlType, typeName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        try {
            getCallableStatement().registerOutParameter(parameterName, sqlType);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        try {
            getCallableStatement().registerOutParameter(parameterName, sqlType, scale);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        try {
            getCallableStatement().registerOutParameter(parameterName, sqlType, typeName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public URL getURL(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getURL(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setURL(String parameterName, URL val) throws SQLException {
        try {
            getCallableStatement().setURL(parameterName, val);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNull(String parameterName, int sqlType) throws SQLException {
        try {
            getCallableStatement().setNull(parameterName, sqlType);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBoolean(String parameterName, boolean x) throws SQLException {
        try {
            getCallableStatement().setBoolean(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setByte(String parameterName, byte x) throws SQLException {
        try {
            getCallableStatement().setByte(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setShort(String parameterName, short x) throws SQLException {
        try {
            getCallableStatement().setShort(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setInt(String parameterName, int x) throws SQLException {
        try {
            getCallableStatement().setInt(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setLong(String parameterName, long x) throws SQLException {
        try {
            getCallableStatement().setLong(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setFloat(String parameterName, float x) throws SQLException {
        try {
            getCallableStatement().setFloat(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setDouble(String parameterName, double x) throws SQLException {
        try {
            getCallableStatement().setDouble(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        try {
            getCallableStatement().setBigDecimal(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setString(String parameterName, String x) throws SQLException {
        try {
            getCallableStatement().setString(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBytes(String parameterName, byte[] x) throws SQLException {
        try {
            getCallableStatement().setBytes(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setDate(String parameterName, Date x) throws SQLException {
        try {
            getCallableStatement().setDate(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setTime(String parameterName, Time x) throws SQLException {
        try {
            getCallableStatement().setTime(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        try {
            getCallableStatement().setTimestamp(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        try {
            getCallableStatement().setAsciiStream(parameterName, x, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        try {
            getCallableStatement().setBinaryStream(parameterName, x, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        try {
            getCallableStatement().setObject(parameterName, x, targetSqlType, scale);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        try {
            getCallableStatement().setObject(parameterName, x, targetSqlType);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setObject(String parameterName, Object x) throws SQLException {
        try {
            getCallableStatement().setObject(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        try {
            getCallableStatement().setCharacterStream(parameterName, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        try {
            getCallableStatement().setDate(parameterName, x, cal);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        try {
            getCallableStatement().setTime(parameterName, x, cal);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        try {
            getCallableStatement().setTimestamp(parameterName, x, cal);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        try {
            getCallableStatement().setNull(parameterName, sqlType, typeName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public String getString(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getString(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public boolean getBoolean(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getBoolean(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public byte getByte(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getByte(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public short getShort(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getShort(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public int getInt(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getInt(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public long getLong(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getLong(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public float getFloat(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getFloat(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public double getDouble(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getDouble(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public byte[] getBytes(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getBytes(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Date getDate(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getDate(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Time getTime(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getTime(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getTimestamp(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Object getObject(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getObject(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getBigDecimal(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        try {
            return getCallableStatement().getObject(parameterName, map);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Ref getRef(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getRef(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Blob getBlob(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getBlob(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Clob getClob(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getClob(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Array getArray(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getArray(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        try {
            return getCallableStatement().getDate(parameterName, cal);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        try {
            return getCallableStatement().getTime(parameterName, cal);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        try {
            return getCallableStatement().getTimestamp(parameterName, cal);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public URL getURL(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getURL(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public RowId getRowId(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getRowId(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public RowId getRowId(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getRowId(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setRowId(String parameterName, RowId x) throws SQLException {
        try {
            getCallableStatement().setRowId(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNString(String parameterName, String value) throws SQLException {
        try {
            getCallableStatement().setNString(parameterName, value);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        try {
            getCallableStatement().setNCharacterStream(parameterName, value, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNClob(String parameterName, NClob value) throws SQLException {
        try {
            getCallableStatement().setNClob(parameterName, value);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        try {
            getCallableStatement().setClob(parameterName, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        try {
            getCallableStatement().setBlob(parameterName, inputStream, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        try {
            getCallableStatement().setNClob(parameterName, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public NClob getNClob(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getNClob(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public NClob getNClob(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getNClob(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        try {
            getCallableStatement().setSQLXML(parameterName, xmlObject);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getSQLXML(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public SQLXML getSQLXML(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getSQLXML(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public String getNString(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getNString(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public String getNString(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getNString(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getNCharacterStream(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Reader getNCharacterStream(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getNCharacterStream(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        try {
            return getCallableStatement().getCharacterStream(parameterIndex);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public Reader getCharacterStream(String parameterName) throws SQLException {
        try {
            return getCallableStatement().getCharacterStream(parameterName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBlob(String parameterName, Blob x) throws SQLException {
        try {
            getCallableStatement().setBlob(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setClob(String parameterName, Clob x) throws SQLException {
        try {
            getCallableStatement().setClob(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        try {
            getCallableStatement().setAsciiStream(parameterName, x, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        try {
            getCallableStatement().setBinaryStream(parameterName, x, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        try {
            getCallableStatement().setCharacterStream(parameterName, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        try {
            getCallableStatement().setAsciiStream(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        try {
            getCallableStatement().setBinaryStream(parameterName, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        try {
            getCallableStatement().setCharacterStream(parameterName, reader);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        try {
            getCallableStatement().setNCharacterStream(parameterName, value);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setClob(String parameterName, Reader reader) throws SQLException {
        try {
            getCallableStatement().setClob(parameterName, reader);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        try {
            getCallableStatement().setBlob(parameterName, inputStream);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNClob(String parameterName, Reader reader) throws SQLException {
        try {
            getCallableStatement().setNClob(parameterName, reader);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        try {
            return getCallableStatement().getObject(parameterIndex, type);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        try {
            return getCallableStatement().getObject(parameterName, type);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setObject(String parameterName, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        try {
            getCallableStatement().setObject(parameterName, x, targetSqlType, scaleOrLength);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setObject(String parameterName, Object x, SQLType targetSqlType) throws SQLException {
        try {
            getCallableStatement().setObject(parameterName, x, targetSqlType);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void registerOutParameter(int parameterIndex, SQLType sqlType) throws SQLException {
        try {
            getCallableStatement().registerOutParameter(parameterIndex, sqlType);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void registerOutParameter(int parameterIndex, SQLType sqlType, int scale) throws SQLException {
        try {
            getCallableStatement().registerOutParameter(parameterIndex, sqlType, scale);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void registerOutParameter(int parameterIndex, SQLType sqlType, String typeName) throws SQLException {
        try {
            getCallableStatement().registerOutParameter(parameterIndex, sqlType, typeName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void registerOutParameter(String parameterName, SQLType sqlType) throws SQLException {
        try {
            getCallableStatement().registerOutParameter(parameterName, sqlType);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void registerOutParameter(String parameterName, SQLType sqlType, int scale) throws SQLException {
        try {
            getCallableStatement().registerOutParameter(parameterName, sqlType, scale);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void registerOutParameter(String parameterName, SQLType sqlType, String typeName) throws SQLException {
        try {
            getCallableStatement().registerOutParameter(parameterName, sqlType, typeName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }
}
