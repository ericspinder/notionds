package com.notionds.dataSource.connection;

import com.notionds.dataSource.*;
import com.notionds.dataSource.connection.accounting.CallableStatementAccounting;
import com.notionds.dataSource.connection.accounting.OperationAccounting;
import com.notionds.dataSource.connection.accounting.PreparedStatementAccounting;
import com.notionds.dataSource.connection.accounting.StatementAccounting;
import com.notionds.dataSource.connection.cleanup.ConnectionCleanup;
import com.notionds.dataSource.connection.delegation.DelegationOfNotion;
import com.notionds.dataSource.connection.cleanup.NotionCleanup;
import com.notionds.dataSource.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class ConnectionContainer<O extends Options,
        EA extends ExceptionAdvice,
        CA extends ConnectionAnalysis,
        D extends DelegationOfNotion,
        CC extends ConnectionCleanup<O, NC>,
        NC extends NotionCleanup<O, CC, VC>,
        VC extends VendorConnection> {

    private final Logger logger = LoggerFactory.getLogger(ConnectionContainer.class);


    private final O options;
    private final UUID connectionId = UUID.randomUUID();
    private final EA exceptionAdvice;
    private final CA connectionAnalysis;
    private final D delegation;
    private final CC connectionCleanup;

    public ConnectionContainer(O options, EA exceptionAdvice, CA connectionAnalysis, VC vendorConnection, D delegation, NC notionCleanup) {
        this.options = options;
        this.exceptionAdvice = exceptionAdvice;
        this.connectionAnalysis = connectionAnalysis;
        this.delegation = delegation; //((Class<W>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor().newInstance(options);
        this.connectionCleanup = notionCleanup.register(vendorConnection);
    }
    public Connection getNotionConnection() {
        return this.connectionCleanup.getConnection(this);
    }

    protected void handleRecommendation(ExceptionAdvice.Recommendation recommendation) {
        //if (ExceptionAdvice.Recommendation.CloseConnectionInstance.equals(recommendation)) {

        //}
    }
    public SQLException handleSQLException(SQLException sqlException, ConnectionMember_I delegatedInstance) {
        SqlExceptionWrapper sqlExceptionWrapper = this.exceptionAdvice.adviseSqlException(sqlException, delegatedInstance.getOperationAccounting());
        this.handleRecommendation(this.connectionAnalysis.reviewException(sqlExceptionWrapper));
        return sqlExceptionWrapper;
    }
    public SQLClientInfoException handleSQLClientInfoExcpetion(SQLClientInfoException sqlClientInfoException, ConnectionMember_I delegatedInstance) {
        SqlClientInfoExceptionWrapper sqlClientInfoExceptionWrapper = this.exceptionAdvice.adviseSQLClientInfoException(sqlClientInfoException, delegatedInstance.getOperationAccounting());
        this.handleRecommendation(this.connectionAnalysis.reviewException(sqlClientInfoExceptionWrapper));
        return sqlClientInfoExceptionWrapper;
    }
    public IOException handleIoException(IOException ioException, ConnectionMember_I delegatedInstance) {
        IoExceptionWrapper ioExceptionWrapper = this.exceptionAdvice.adviseIoException(ioException, delegatedInstance.getOperationAccounting());
        this.handleRecommendation(this.connectionAnalysis.reviewException(ioExceptionWrapper));
        return  ioExceptionWrapper;
    }
    public Exception handleException(Exception exception, ConnectionMember_I delegatedInstance) {
        ExceptionWrapper exceptionWrapper = this.exceptionAdvice.adviseException(exception, delegatedInstance.getOperationAccounting());
        this.handleRecommendation((this.connectionAnalysis.reviewException(exceptionWrapper)));
        return exceptionWrapper;
    }

    public final UUID getConnectionId() {
        return this.connectionId;
    }

    public final CC getConnectionCleanup() {
        return this.connectionCleanup;
    }
/*
    public void closeSqlException(ConnectionMember_I delegatedInstance) throws SQLException {

    }
    public void closeIoException(ConnectionMember_I delegatedInstance) throws IOException {
        if (delegatedInstance instanceof Flushable) {
            try {
                ((Flushable)delegatedInstance).flush();
            }
            catch (IOException ioe) {
                // eat it, closing anyways
            }
        }
        if (delegatedInstance instanceof Closeable) {

        }

    }
    public void closeFree(ConnectionMember_I delgatedInstance) throws SQLException {

    }
    public void closeNotNeeded(ConnectionMember_I delegatedInstance) {
        this.connectionCleanup.close(delegatedInstance);
    }
*/

    @SuppressWarnings("unchecked")
    public ConnectionMember_I wrap(Object delegate, Class clazz, ConnectionMember_I parent, String maybeSql) {
        OperationAccounting operationAccounting = null;
        if (clazz.isAssignableFrom(Statement.class)) {
            if (clazz.isAssignableFrom(CallableStatement.class)) {
                operationAccounting = new CallableStatementAccounting(this.getConnectionId(), maybeSql);
            }
            else if (clazz.isAssignableFrom(PreparedStatement.class)) {
                operationAccounting = new PreparedStatementAccounting(this.connectionId, maybeSql);
            }
            else {
                operationAccounting = new StatementAccounting(this.getConnectionId());
            }
        }
        else {
            operationAccounting = new OperationAccounting(this.getConnectionId());
        }
        ConnectionMember_I wrapped = delegation.getDelegate(this, delegate, clazz.getClass(), operationAccounting);
        this.connectionCleanup.add(wrapped, delegate, parent);
        return wrapped;
    }

}
