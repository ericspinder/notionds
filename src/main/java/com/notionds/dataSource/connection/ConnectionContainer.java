package com.notionds.dataSource.connection;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.accounting.OperationAccounting;
import com.notionds.dataSource.connection.cleanup.ConnectionCleanup;
import com.notionds.dataSource.connection.cleanup.NotionCleanup;
import com.notionds.dataSource.connection.delegation.DelegationOfNotion;
import com.notionds.dataSource.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.UUID;

public class ConnectionContainer<O extends Options,
        EA extends ExceptionAdvice,
        CA extends ConnectionAnalysis,
        D extends DelegationOfNotion,
        CC extends ConnectionCleanup<O, NC, VC>,
        NC extends NotionCleanup<O, CC, VC>,
        VC extends VendorConnection> {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionContainer.class);

    private final O options;
    private final UUID connectionId = UUID.randomUUID();
    private final EA exceptionAdvice;
    private final D delegation;
    private final CC connectionCleanup;

    public ConnectionContainer(O options, EA exceptionAdvice, VC vendorConnection, D delegation, NC notionCleanup) {
        this.options = options;
        this.exceptionAdvice = exceptionAdvice;
        this.delegation = delegation;
        this.connectionCleanup = notionCleanup.register(vendorConnection);
    }
    public Connection getNotionConnection() {
        return this.connectionCleanup.getConnection(this);
    }

    public SQLException handleSQLException(SQLException sqlException, ConnectionMember_I delegatedInstance) {
        SqlExceptionWrapper sqlExceptionWrapper = this.exceptionAdvice.adviseSqlException(sqlException, delegatedInstance.getOperationAccounting());
        this.connectionCleanup.reviewException(sqlExceptionWrapper);
        return sqlExceptionWrapper;
    }
    public SQLClientInfoException handleSQLClientInfoExcpetion(SQLClientInfoException sqlClientInfoException, ConnectionMember_I delegatedInstance) {
        SqlClientInfoExceptionWrapper sqlClientInfoExceptionWrapper = this.exceptionAdvice.adviseSQLClientInfoException(sqlClientInfoException, delegatedInstance.getOperationAccounting());
        this.connectionCleanup.reviewException(sqlClientInfoExceptionWrapper);
        return sqlClientInfoExceptionWrapper;
    }
    public IOException handleIoException(IOException ioException, ConnectionMember_I delegatedInstance) {
        IoExceptionWrapper ioExceptionWrapper = this.exceptionAdvice.adviseIoException(ioException, delegatedInstance.getOperationAccounting());
        this.connectionCleanup.reviewException(ioExceptionWrapper);
        return  ioExceptionWrapper;
    }
    public Exception handleException(Exception exception, ConnectionMember_I delegatedInstance) {
        ExceptionWrapper exceptionWrapper = this.exceptionAdvice.adviseException(exception, delegatedInstance.getOperationAccounting());
        this.connectionCleanup.reviewException(exceptionWrapper);
        return exceptionWrapper;
    }

    public final UUID getConnectionId() {
        return this.connectionId;
    }

    public final CC getConnectionCleanup() {
        return this.connectionCleanup;
    }

    public ConnectionMember_I wrap(Object delegate, Class clazz, ConnectionMember_I parent) {
        return this.wrap(delegate, clazz, parent, null);
    }

    public ConnectionMember_I wrap(Object delegate, Class clazz, ConnectionMember_I parent, String maybeSql) {
        OperationAccounting operationAccounting = this.connectionAnalysis.createAccounting(clazz, this.connectionId, maybeSql);
        ConnectionMember_I wrapped = delegation.getDelegate(this, delegate, operationAccounting);
        this.connectionCleanup.add(wrapped, delegate, parent);
        return wrapped;
    }
}
