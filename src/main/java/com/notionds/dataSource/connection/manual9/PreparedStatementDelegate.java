package com.notionds.dataSource.connection.manual9;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public class PreparedStatementDelegate extends StatementDelegate implements PreparedStatement {

     
    public PreparedStatementDelegate(ConnectionContainerManual9 notionWrapper, PreparedStatement delegate) {
        super(notionWrapper, delegate);
    }
    private PreparedStatement getPreparedStatement() {
        return (PreparedStatement)delegate;
    }

    public ResultSet executeQuery() throws SQLException {
        try {
            return getPreparedStatement().executeQuery();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public int executeUpdate() throws SQLException {
        try {
            return getPreparedStatement().executeUpdate();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        try {
            getPreparedStatement().setNull(parameterIndex, sqlType);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        try {
            getPreparedStatement().setBoolean(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        try {
            getPreparedStatement().setByte(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        try {
            getPreparedStatement().setShort(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        try {
            getPreparedStatement().setInt(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        try {
            getPreparedStatement().setLong(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        try {
            getPreparedStatement().setFloat(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        try {
            getPreparedStatement().setDouble(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        try {
            getPreparedStatement().setBigDecimal(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        try {
            getPreparedStatement().setString(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        try {
            getPreparedStatement().setBytes(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        try {
            getPreparedStatement().setDate(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        try {
            getPreparedStatement().setTime(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        try {
            getPreparedStatement().setTimestamp(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        try {
            getPreparedStatement().setAsciiStream(parameterIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        try {
            getPreparedStatement().setUnicodeStream(parameterIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        try {
            getPreparedStatement().setBinaryStream(parameterIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void clearParameters() throws SQLException {
        try {
            getPreparedStatement().clearParameters();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        try {
            getPreparedStatement().setObject(parameterIndex, x, targetSqlType);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setObject(int parameterIndex, Object x) throws SQLException {
        try {
            getPreparedStatement().setObject(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public boolean execute() throws SQLException {
        try {
            return getPreparedStatement().execute();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void addBatch() throws SQLException {
        try {
            getPreparedStatement().addBatch();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        try {
            getPreparedStatement().setCharacterStream(parameterIndex, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setRef(int parameterIndex, Ref x) throws SQLException {
        try {
            getPreparedStatement().setRef(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        try {
            getPreparedStatement().setBlob(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setClob(int parameterIndex, Clob x) throws SQLException {
        try {
            getPreparedStatement().setClob(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setArray(int parameterIndex, Array x) throws SQLException {
        try {
            getPreparedStatement().setArray(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        try {
            return getPreparedStatement().getMetaData();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        try {
            getPreparedStatement().setDate(parameterIndex, x, cal);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        try {
            getPreparedStatement().setTime(parameterIndex, x, cal);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        try {
            getPreparedStatement().setTimestamp(parameterIndex, x, cal);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        try {
            getPreparedStatement().setNull(parameterIndex, sqlType, typeName);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        try {
            getPreparedStatement().setURL(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        try {
            return getPreparedStatement().getParameterMetaData();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        try {
            getPreparedStatement().setRowId(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNString(int parameterIndex, String value) throws SQLException {
        try {
            getPreparedStatement().setNString(parameterIndex, value);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        try {
            getPreparedStatement().setNCharacterStream(parameterIndex, value, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        try {
            getPreparedStatement().setNClob(parameterIndex, value);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        try {
            getPreparedStatement().setClob(parameterIndex, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        try {
            getPreparedStatement().setBlob(parameterIndex, inputStream, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        try {
            getPreparedStatement().setNClob(parameterIndex, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        try {
            getPreparedStatement().setSQLXML(parameterIndex, xmlObject);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        try {
            getPreparedStatement().setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        try {
            getPreparedStatement().setAsciiStream(parameterIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        try {
            getPreparedStatement().setBinaryStream(parameterIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        try {
            getPreparedStatement().setCharacterStream(parameterIndex, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        try {
            getPreparedStatement().setAsciiStream(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        try {
            getPreparedStatement().setBinaryStream(parameterIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        try {
            getPreparedStatement().setCharacterStream(parameterIndex, reader);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        try {
            getPreparedStatement().setNCharacterStream(parameterIndex, value);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        try {
            getPreparedStatement().setClob(parameterIndex, reader);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        try {
            getPreparedStatement().setBlob(parameterIndex, inputStream);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        try {
            getPreparedStatement().setNClob(parameterIndex, reader);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        try {
            getPreparedStatement().setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
        try {
            getPreparedStatement().setObject(parameterIndex, x, targetSqlType);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    public long executeLargeUpdate() throws SQLException {
        try {
            return getPreparedStatement().executeLargeUpdate();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }
}
