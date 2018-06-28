package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.*;

import java.io.InputStream;
import java.io.Reader;
import java.sql.*;

public abstract class NotionWrapperManual9<O extends Options> extends NotionWrapper<O, NotionConnectionDelegate> {

    public NotionWrapperManual9(O options, VendorConnection vendorConnection) {
        super(options, vendorConnection);
    }

    public abstract void close(ConnectionMember_I delegatedInstance);

    public StatementDelegate wrap(Statement statement, ConnectionMember_I parent) throws SQLException {
        StatementDelegate statementDelegate = new StatementDelegate(this, statement);
        NotionWeakReference<StatementDelegate> weakReference = new NotionWeakReference<StatementDelegate>(statementDelegate, this);
        return statementDelegate;

    }
    public PreparedStatementDelegate wrap(PreparedStatement preparedStatement, ConnectionMember_I parent) throws SQLException {
        PreparedStatementDelegate preparedStatementDelegate = new PreparedStatementDelegate(this, preparedStatement);
        NotionWeakReference<PreparedStatementDelegate> weakReference = new NotionWeakReference<PreparedStatementDelegate>(preparedStatementDelegate, this);
        return preparedStatementDelegate;
    }
    public CallableStatementDelegate wrap(CallableStatement callableStatement, ConnectionMember_I parent) throws SQLException {
        CallableStatementDelegate callableStatementDelegate = new CallableStatementDelegate(this, callableStatement);
        NotionWeakReference<CallableStatementDelegate> weakReference = new NotionWeakReference<CallableStatementDelegate>(callableStatementDelegate, this);
        return callableStatementDelegate;
    }
    public DatabaseMetaDataDelegate wrap(DatabaseMetaData databaseMetaData, ConnectionMember_I parent) throws SQLException {
        DatabaseMetaDataDelegate databaseMetaDataDelegate = new DatabaseMetaDataDelegate(this, databaseMetaData);
        NotionWeakReference<DatabaseMetaDataDelegate> weakReference = new NotionWeakReference<DatabaseMetaDataDelegate>(databaseMetaDataDelegate, this);
        return databaseMetaDataDelegate;
    }
    public NClobDelegate wrap(NClob nClob, ConnectionMember_I parent) throws SQLException {
        NClobDelegate nClobDelegate = new NClobDelegate(this, nClob);
        NotionWeakReference<NClobDelegate> weakReference = new NotionWeakReference<NClobDelegate>(nClobDelegate, this);
        return nClobDelegate;
    }
    public ClobDelegate wrap(Clob clob, ConnectionMember_I parent) throws SQLException {
        ClobDelegate clobDelegate = new ClobDelegate(this, clob);
        NotionWeakReference<ClobDelegate> weakReference = new NotionWeakReference<ClobDelegate>(clobDelegate, this);
        return clobDelegate;
    }
    public BlobDelegate wrap(Blob blob, ConnectionMember_I parent)throws SQLException {
        BlobDelegate blobDelegate = new BlobDelegate(this, blob);
        NotionWeakReference<BlobDelegate> weakReference = new NotionWeakReference<BlobDelegate>(blobDelegate, this);
        return blobDelegate;
    }
    public ResultSetDelegate wrap(ResultSet resultSet, ConnectionMember_I parent) throws SQLException {
        ResultSetDelegate resultSetDelegate = new ResultSetDelegate(this, resultSet);
        NotionWeakReference<ResultSetDelegate> weakReference = new NotionWeakReference<ResultSetDelegate>(resultSetDelegate, this);
        return resultSetDelegate;
    }
    public InputStreamDelegate wrap(InputStream inputStream, ConnectionMember_I parent) throws SQLException {
        InputStreamDelegate inputStreamDelegate = new InputStreamDelegate(this, inputStream);
        NotionWeakReference<InputStreamDelegate> weakReference = new NotionWeakReference<InputStreamDelegate>(inputStreamDelegate, this);
        return inputStreamDelegate;
    }
    public Reader wrap(Reader reader, ConnectionMember_I parent) throws SQLException {
        ReaderDelegate readerDelegate = new ReaderDelegate(this, reader);
        NotionWeakReference<ReaderDelegate> weakReference = new NotionWeakReference<ReaderDelegate>(readerDelegate, this);
        return readerDelegate;
    }

    public abstract void close() throws SQLException;
}
