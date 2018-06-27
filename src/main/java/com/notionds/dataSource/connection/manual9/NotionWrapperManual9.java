package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionAnalysis;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.ExceptionAdvice;
import com.notionds.dataSource.connection.NotionWrapper;
import com.notionds.dataSource.connection.VendorConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.UUID;

public abstract class NotionWrapperManual9<O extends Options> extends NotionWrapper<O, NotionConnectionDelegate> {

    public NotionWrapperManual9(O options, VendorConnection vendorConnection) {
        super(options, vendorConnection);


    }

    public abstract void close(ConnectionMember_I delegatedInstance);

    protected abstract void setNotionConnectionTree(NotionConnectionDelegate notionConnectionTree);

    public StatementDelegate wrap(Statement statement, ConnectionMember_I parent) throws SQLException {
        StatementDelegate statementDelegate = new StatementDelegate(this, statement);
        this.delegateTree.put(parent, statementDelegate);
        return statementDelegate;

    }
    public PreparedStatementDelegate wrap(PreparedStatement preparedStatement, ConnectionMember_I parent) throws SQLException {
        PreparedStatementDelegate preparedStatementDelegate = new PreparedStatementDelegate(this, preparedStatement);
        this.delegateTree.put(parent, preparedStatementDelegate);
        return preparedStatementDelegate;
    }
    public CallableStatementDelegate wrap(CallableStatement callableStatement, ConnectionMember_I parent) throws SQLException {
        CallableStatementDelegate callableStatementDelegate = new CallableStatementDelegate(this, callableStatement);
        this.delegateTree.put(parent, callableStatementDelegate);
        return callableStatementDelegate;
    }
    public DatabaseMetaDataDelegate wrap(DatabaseMetaData databaseMetaData, ConnectionMember_I parent) throws SQLException {
        DatabaseMetaDataDelegate databaseMetaDataDelegate = new DatabaseMetaDataDelegate(this, databaseMetaData);
        this.delegateTree.put(parent, databaseMetaDataDelegate);
        return databaseMetaDataDelegate;
    }
    public NClobDelegate wrap(NClob nClob, ConnectionMember_I parent) throws SQLException {
        NClobDelegate nClobDelegate = new NClobDelegate(this, nClob);
        this.delegateTree.put(parent, nClobDelegate);
        return nClobDelegate;
    }
    public ClobDelegate wrap(Clob clob, ConnectionMember_I parent) throws SQLException {
        ClobDelegate clobDelegate = new ClobDelegate(this, clob);
        this.delegateTree.put(parent, clobDelegate);
        return clobDelegate;
    }
    public BlobDelegate wrap(Blob blob, ConnectionMember_I parent)throws SQLException {
        BlobDelegate blobDelegate = new BlobDelegate(this, blob);
        this.delegateTree.put(parent, blobDelegate);
        return blobDelegate;
    }
    public ResultSetDelegate wrap(ResultSet resultSet, ConnectionMember_I parent) throws SQLException {
        ResultSetDelegate resultSetDelegate = new ResultSetDelegate(this, resultSet);
        this.delegateTree.put(parent, resultSetDelegate);
        return resultSetDelegate;
    }
    public InputStreamDelegate wrap(InputStream inputStream, ConnectionMember_I parent) throws SQLException {
        InputStreamDelegate inputStreamDelegate = new InputStreamDelegate(this, inputStream);
        this.delegateTree.put(parent, inputStreamDelegate);
        return inputStreamDelegate;
    }
    public Reader wrap(Reader reader, ConnectionMember_I parent) throws SQLException {
        ReaderDelegate readerDelegate = new ReaderDelegate(this, reader);
        this.delegateTree.put(parent, readerDelegate);
        return readerDelegate;
    }
    public abstract NotionConnectionDelegate retrieve(Connection delegatedConnection, Statement child) throws SQLException;

    public abstract void close() throws SQLException;
}
