package com.notionds.dataSource.delegate;

import com.notionds.dataSource.DelegateMapper;
import com.notionds.dataSource.DelegatedInstance;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public class ResultSetDelegate<DM extends DelegateMapper<?,?,?>> extends DelegatedInstance<DM, ResultSet> implements ResultSet {
    
    public ResultSetDelegate(DM delegateMapper, ResultSet delegate) {
        super(delegateMapper, delegate);
    }

    public boolean next() throws SQLException {
        try {
            return delegate.next();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void close() throws SQLException {
        try {
            delegate.close();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean wasNull() throws SQLException {
        try {
            return delegate.wasNull();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public String getString(int columnIndex) throws SQLException {
        try {
            return delegate.getString(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        try {
            return delegate.getBoolean(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public byte getByte(int columnIndex) throws SQLException {
        try {
            return delegate.getByte(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public short getShort(int columnIndex) throws SQLException {
        try {
            return delegate.getShort(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public int getInt(int columnIndex) throws SQLException {
        try {
            return delegate.getInt(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public long getLong(int columnIndex) throws SQLException {
        try {
            return delegate.getLong(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public float getFloat(int columnIndex) throws SQLException {
        try {
            return delegate.getFloat(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public double getDouble(int columnIndex) throws SQLException {
        try {
            return delegate.getDouble(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Deprecated(since = "1.2")
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        try {
            return delegate.getBigDecimal(columnIndex, scale);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        try {
            return delegate.getBytes(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Date getDate(int columnIndex) throws SQLException {
        try {
            return delegate.getDate(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Time getTime(int columnIndex) throws SQLException {
        try {
            return delegate.getTime(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        try {
            return delegate.getTimestamp(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        try {
            return delegate.getAsciiStream(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Deprecated(since = "1.2")
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getUnicodeStream(columnIndex), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getBinaryStream(columnIndex), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public String getString(String columnLabel) throws SQLException {
        try {
            return delegate.getString(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean getBoolean(String columnLabel) throws SQLException {
        try {
            return delegate.getBoolean(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public byte getByte(String columnLabel) throws SQLException {
        try {
            return delegate.getByte(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public short getShort(String columnLabel) throws SQLException {
        try {
            return delegate.getShort(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public int getInt(String columnLabel) throws SQLException {
        try {
            return delegate.getInt(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public long getLong(String columnLabel) throws SQLException {
        try {
            return delegate.getLong(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public float getFloat(String columnLabel) throws SQLException {
        try {
            return delegate.getFloat(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public double getDouble(String columnLabel) throws SQLException {
        try {
            return delegate.getDouble(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Deprecated(since = "1.2")
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        try {
            return delegate.getBigDecimal(columnLabel, scale);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public byte[] getBytes(String columnLabel) throws SQLException {
        try {
            return delegate.getBytes(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Date getDate(String columnLabel) throws SQLException {
        try {
            return delegate.getDate(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Time getTime(String columnLabel) throws SQLException {
        try {
            return delegate.getTime(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        try {
            return delegate.getTimestamp(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getAsciiStream(columnLabel), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    @Deprecated(since = "1.2")
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getUnicodeStream(columnLabel), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getBinaryStream(columnLabel), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public SQLWarning getWarnings() throws SQLException {
        try {
            return delegate.getWarnings();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void clearWarnings() throws SQLException {
        try {
            delegate.clearWarnings();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public String getCursorName() throws SQLException {
        try {
            return delegate.getCursorName();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        try {
            return delegate.getMetaData();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Object getObject(int columnIndex) throws SQLException {
        try {
            return delegate.getObject(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Object getObject(String columnLabel) throws SQLException {
        try {
            return delegate.getObject(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public int findColumn(String columnLabel) throws SQLException {
        try {
            return delegate.findColumn(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getCharacterStream(columnIndex), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Reader getCharacterStream(String columnLabel) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getCharacterStream(columnLabel), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        try {
            return delegate.getBigDecimal(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        try {
            return delegate.getBigDecimal(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean isBeforeFirst() throws SQLException {
        try {
            return delegate.isBeforeFirst();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean isAfterLast() throws SQLException {
        try {
            return delegate.isAfterLast();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean isFirst() throws SQLException {
        try {
            return delegate.isFirst();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean isLast() throws SQLException {
        try {
            return delegate.isLast();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void beforeFirst() throws SQLException {
        try {
            delegate.beforeFirst();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void afterLast() throws SQLException {
        try {
            delegate.afterLast();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean first() throws SQLException {
        try {
            return delegate.first();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean last() throws SQLException {
        try {
            return delegate.last();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public int getRow() throws SQLException {
        try {
            return delegate.getRow();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean absolute(int row) throws SQLException {
        try {
            return delegate.absolute(row);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean relative(int rows) throws SQLException {
        try {
            return delegate.relative(rows);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean previous() throws SQLException {
        try {
            return delegate.previous();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void setFetchDirection(int direction) throws SQLException {
        try {
            delegate.setFetchDirection(direction);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public int getFetchDirection() throws SQLException {
        try {
            return delegate.getFetchDirection();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void setFetchSize(int rows) throws SQLException {
        try {
            delegate.setFetchSize(rows);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public int getFetchSize() throws SQLException {
        try {
            return delegate.getFetchSize();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public int getType() throws SQLException {
        try {
            return delegate.getType();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public int getConcurrency() throws SQLException {
        try {
            return delegate.getConcurrency();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean rowUpdated() throws SQLException {
        try {
            return delegate.rowUpdated();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean rowInserted() throws SQLException {
        try {
            return delegate.rowInserted();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean rowDeleted() throws SQLException {
        try {
            return delegate.rowDeleted();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNull(int columnIndex) throws SQLException {
        try {
            delegate.updateNull(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        try {
            delegate.updateBoolean(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateByte(int columnIndex, byte x) throws SQLException {
        try {
            delegate.updateByte(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateShort(int columnIndex, short x) throws SQLException {
        try {
            delegate.updateShort(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateInt(int columnIndex, int x) throws SQLException {
        try {
            delegate.updateInt(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateLong(int columnIndex, long x) throws SQLException {
        try {
            delegate.updateLong(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateFloat(int columnIndex, float x) throws SQLException {
        try {
            delegate.updateFloat(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateDouble(int columnIndex, double x) throws SQLException {
        try {
            delegate.updateDouble(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        try {
            delegate.updateBigDecimal(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateString(int columnIndex, String x) throws SQLException {
        try {
            delegate.updateString(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        try {
            delegate.updateBytes(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateDate(int columnIndex, Date x) throws SQLException {
        try {
            delegate.updateDate(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateTime(int columnIndex, Time x) throws SQLException {
        try {
            delegate.updateTime(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        try {
            delegate.updateTimestamp(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        try {
            delegate.updateAsciiStream(columnIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        try {
            delegate.updateBinaryStream(columnIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        try {
            delegate.updateCharacterStream(columnIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        try {
            delegate.updateObject(columnIndex, x, scaleOrLength);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateObject(int columnIndex, Object x) throws SQLException {
        try {
            delegate.updateObject(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNull(String columnLabel) throws SQLException {
        try {
            delegate.updateNull(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        try {
            delegate.updateBoolean(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateByte(String columnLabel, byte x) throws SQLException {
        try {
            delegate.updateByte(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateShort(String columnLabel, short x) throws SQLException {
        try {
            delegate.updateShort(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateInt(String columnLabel, int x) throws SQLException {
        try {
            delegate.updateInt(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateLong(String columnLabel, long x) throws SQLException {
        try {
            delegate.updateLong(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateFloat(String columnLabel, float x) throws SQLException {
        try {
            delegate.updateFloat(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateDouble(String columnLabel, double x) throws SQLException {
        try {
            delegate.updateDouble(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        try {
            delegate.updateBigDecimal(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateString(String columnLabel, String x) throws SQLException {
        try {
            delegate.updateString(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        try {
            delegate.updateBytes(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateDate(String columnLabel, Date x) throws SQLException {
        try {
            delegate.updateDate(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateTime(String columnLabel, Time x) throws SQLException {
        try {
            delegate.updateTime(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        try {
            delegate.updateTimestamp(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        try {
            delegate.updateAsciiStream(columnLabel, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        try {
            delegate.updateBinaryStream(columnLabel, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        try {
            delegate.updateCharacterStream(columnLabel, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        try {
            delegate.updateObject(columnLabel, x, scaleOrLength);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateObject(String columnLabel, Object x) throws SQLException {
        try {
            delegate.updateObject(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void insertRow() throws SQLException {
        try {
            delegate.insertRow();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateRow() throws SQLException {
        try {
            delegate.updateRow();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void deleteRow() throws SQLException {
        try {
            delegate.deleteRow();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void refreshRow() throws SQLException {
        try {
            delegate.refreshRow();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void cancelRowUpdates() throws SQLException {
        try {
            delegate.cancelRowUpdates();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void moveToInsertRow() throws SQLException {
        try {
            delegate.moveToInsertRow();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void moveToCurrentRow() throws SQLException {
        try {
            delegate.moveToCurrentRow();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Statement getStatement() throws SQLException {
        try {
            return delegateMapper.retrieve(delegate.getStatement(), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        try {
            return delegate.getObject(columnIndex, map);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Ref getRef(int columnIndex) throws SQLException {
        try {
            return delegate.getRef(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Blob getBlob(int columnIndex) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getBlob(columnIndex), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Clob getClob(int columnIndex) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getClob(columnIndex), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Array getArray(int columnIndex) throws SQLException {
        try {
            return delegate.getArray(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        try {
            return delegate.getObject(columnLabel, map);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Ref getRef(String columnLabel) throws SQLException {
        try {
            return delegate.getRef(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Blob getBlob(String columnLabel) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getBlob(columnLabel), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Clob getClob(String columnLabel) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getClob(columnLabel), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Array getArray(String columnLabel) throws SQLException {
        try {
            return delegate.getArray(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        try {
            return delegate.getDate(columnIndex, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        try {
            return delegate.getDate(columnLabel, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        try {
            return delegate.getTime(columnIndex, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        try {
            return delegate.getTime(columnLabel, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        try {
            return delegate.getTimestamp(columnIndex, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        try {
            return delegate.getTimestamp(columnLabel, cal);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public URL getURL(int columnIndex) throws SQLException {
        try {
            return delegate.getURL(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public URL getURL(String columnLabel) throws SQLException {
        try {
            return delegate.getURL(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateRef(int columnIndex, Ref x) throws SQLException {
        try {
            delegate.updateRef(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateRef(String columnLabel, Ref x) throws SQLException {
        try {
            delegate.updateRef(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        try {
            delegate.updateBlob(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        try {
            delegate.updateBlob(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateClob(int columnIndex, Clob x) throws SQLException {
        try {
            delegate.updateClob(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateClob(String columnLabel, Clob x) throws SQLException {
        try {
            delegate.updateClob(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateArray(int columnIndex, Array x) throws SQLException {
        try {
            delegate.updateArray(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateArray(String columnLabel, Array x) throws SQLException {
        try {
            delegate.updateArray(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public RowId getRowId(int columnIndex) throws SQLException {
        try {
            return delegate.getRowId(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public RowId getRowId(String columnLabel) throws SQLException {
        try {
            return delegate.getRowId(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        try {
            delegate.updateRowId(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        try {
            delegate.updateRowId(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public int getHoldability() throws SQLException {
        try {
            return delegate.getHoldability();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean isClosed() throws SQLException {
        try {
            return delegate.isClosed();
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNString(int columnIndex, String nString) throws SQLException {
        try {
            delegate.updateNString(columnIndex, nString);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNString(String columnLabel, String nString) throws SQLException {
        try {
            delegate.updateNString(columnLabel, nString);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        try {
            delegate.updateNClob(columnIndex, nClob);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        try {
            delegate.updateNClob(columnLabel, nClob);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public NClob getNClob(int columnIndex) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getNClob(columnIndex), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public NClob getNClob(String columnLabel) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getNClob(columnLabel), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        try {
                return delegate.getSQLXML(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        try {
            return delegate.getSQLXML(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        try {
            delegate.updateSQLXML(columnIndex, xmlObject);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        try {
            delegate.updateSQLXML(columnLabel, xmlObject);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public String getNString(int columnIndex) throws SQLException {
        try {
            return delegate.getNString(columnIndex);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public String getNString(String columnLabel) throws SQLException {
        try {
            return delegate.getNString(columnLabel);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getNCharacterStream(columnIndex), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        try {
            return delegateMapper.wrap(delegate.getNCharacterStream(columnLabel), this);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        try {
            delegate.updateNCharacterStream(columnIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        try {
            delegate.updateNCharacterStream(columnLabel, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        try {
            delegate.updateAsciiStream(columnIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        try {
            delegate.updateBinaryStream(columnIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        try {
            delegate.updateCharacterStream(columnIndex, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        try {
            delegate.updateAsciiStream(columnLabel, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        try {
            delegate.updateBinaryStream(columnLabel, x, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        try {
            delegate.updateCharacterStream(columnLabel, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        try {
            delegate.updateBlob(columnIndex, inputStream, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        try {
            delegate.updateBlob(columnLabel, inputStream, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        try {
            delegate.updateClob(columnIndex, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        try {
            delegate.updateClob(columnLabel, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        try {
            delegate.updateNClob(columnIndex, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        try {
            delegate.updateNClob(columnLabel, reader, length);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        try {
            delegate.updateNCharacterStream(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        try {
            delegate.updateNCharacterStream(columnLabel, reader);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        try {
            delegate.updateAsciiStream(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        try {
            delegate.updateBinaryStream(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        try {
            delegate.updateCharacterStream(columnIndex, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        try {
            delegate.updateAsciiStream(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        try {
            delegate.updateBinaryStream(columnLabel, x);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        try {
            delegate.updateCharacterStream(columnLabel, reader);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        try {
            delegate.updateBlob(columnIndex, inputStream);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        try {
            delegate.updateBlob(columnLabel, inputStream);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        try {
            delegate.updateClob(columnIndex, reader);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        try {
            delegate.updateClob(columnLabel, reader);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        try {
            delegate.updateNClob(columnIndex, reader);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        try {
            delegate.updateNClob(columnLabel, reader);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        try {
            return delegate.getObject(columnIndex, type);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        try {
            return delegate.getObject(columnLabel, type);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        try {
            delegate.updateObject(columnIndex, x, targetSqlType, scaleOrLength);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        try {
            delegate.updateObject(columnLabel, x, targetSqlType, scaleOrLength);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
        try {
            delegate.updateObject(columnIndex, x, targetSqlType);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {
        try {
            delegate.updateObject(columnLabel, x, targetSqlType);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return delegate.unwrap(iface);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        try {
            return delegate.isWrapperFor(iface);
        }
        catch (SQLException sqlException) {
            throw this.delegateMapper.getExceptionHandler().handle(sqlException, this);
        }
    }
}
