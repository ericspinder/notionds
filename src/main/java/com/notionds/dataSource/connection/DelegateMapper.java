package com.notionds.dataSource.connection;

import com.notionds.dataSource.Options;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;

public abstract class DelegateMapper<O extends Options,
        NC extends NotionConnection,
        EH extends ExceptionHandler,
        SD extends Statement,
        PSD extends PreparedStatement,
        CSD extends CallableStatement,
        RSD extends ResultSet,
        DMD extends DatabaseMetaData,
        CBD extends Clob,
        NCD extends NClob,
        BBD extends Blob> {

    private final EH exceptionHandler;
    private final O options;

    public DelegateMapper(O options, EH exceptionHandler) {
        this.options = options;
        this.exceptionHandler = exceptionHandler;
    }

    public ExceptionHandler getExceptionHandler() {
        return this.exceptionHandler;
    }
    public SQLException handle(SQLException sqlException, DelegatedInstance where) {

    }
    public SQLClientInfoException handle(SQLClientInfoException sqlClientInfoException, DelegatedInstance where) {

    }
    public IOException handle(IOException ioException, DelegatedInstance where) {

    }
    protected abstract SQLException closeOnException(SQLException sqlException);
    protected abstract SQLClientInfoException closeOnException(SQLClientInfoException sqlClientInfoException);

    protected abstract void setNotionConnection(NC notionConnection);
    protected abstract NC getNotionConnection();

    public abstract SD wrap(Statement statement, AutoCloseable parent) throws SQLException;
    public abstract PSD wrap(PreparedStatement preparedStatement, AutoCloseable parent) throws SQLException;
    public abstract CSD wrap(CallableStatement callableStatement, AutoCloseable parent) throws SQLException;
    public abstract DMD wrap(DatabaseMetaData databaseMetaData, AutoCloseable parent) throws SQLException;
    public abstract NCD wrap(NClob nClob, AutoCloseable parent) throws SQLException;
    public abstract CBD wrap(Clob clob, AutoCloseable parent) throws SQLException;
    public abstract BBD wrap(Blob blob, AutoCloseable parent)throws SQLException;
    public abstract RSD wrap(ResultSet resultSet, AutoCloseable parent) throws SQLException;
    public abstract InputStream notify(InputStream inputStream, AutoCloseable parent) throws SQLException;
    public abstract Reader Notify(Reader reader, AutoCloseable parent) throws SQLException;

    public abstract SD retrieve(Statement statement, ResultSet child) throws SQLException;
    public abstract NC retrieve(Connection delegatedConnection, Statement child) throws SQLException;

    public abstract void close() throws SQLException;
}
