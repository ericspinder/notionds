package com.notionds.dataSource;

import com.notionds.dataSource.NotionConnection;

import java.io.InputStream;
import java.io.Reader;
import java.sql.*;

public abstract class DelegateMapper<O extends Options, NC extends NotionConnection, EH extends ExceptionHandler> {

    private final EH exceptionHandler;
    private final O options;

    public DelegateMapper(O options, EH exceptionHandler) {
        this.options = options;
        this.exceptionHandler = exceptionHandler;
    }

    public ExceptionHandler getExceptionHandler() {
        return this.exceptionHandler;
    }
    protected abstract void setNotionConnection(NC notionConnection);
    protected abstract NC getNotionConnection();

    public abstract Statement wrap(Statement statement, AutoCloseable parent) throws SQLException;
    public abstract PreparedStatement wrap(PreparedStatement preparedStatement, AutoCloseable parent) throws SQLException;
    public abstract CallableStatement wrap(CallableStatement callableStatement, AutoCloseable parent) throws SQLException;
    public abstract DatabaseMetaData wrap(DatabaseMetaData databaseMetaData, AutoCloseable parent) throws SQLException;
    public abstract NClob wrap(NClob nClob, AutoCloseable parent) throws SQLException;
    public abstract Clob wrap(Clob clob, AutoCloseable parent) throws SQLException;
    public abstract Blob wrap(Blob blob, AutoCloseable parent)throws SQLException;
    public abstract ResultSet wrap(ResultSet resultSet, AutoCloseable parent) throws SQLException;
    public abstract InputStream wrap(InputStream inputStream, AutoCloseable parent) throws SQLException;
    public abstract Reader wrap(Reader reader, AutoCloseable parent) throws SQLException;

    public abstract Statement retrieve(Statement statement, ResultSet child) throws SQLException;
    public abstract NC retrieve(Connection delegatedConnection, Statement child) throws SQLException;

    public abstract void close() throws SQLException;
}
