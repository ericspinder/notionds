package com.notionds.dataSource.connection;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
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

    public ConnectionContainer(O options, EA exceptionAdvice, D delegation, NC notionCleanup, VC vendorConnection) {
        this.options = options;
        this.exceptionAdvice = exceptionAdvice;
        this.delegation = delegation;
        this.connectionCleanup = notionCleanup.register(vendorConnection);
    }
    public Connection getNotionConnection() {
        return this.connectionCleanup.getConnection(this);
    }

    public SQLException handleSQLException(SQLException sqlException, ConnectionMember_I delegatedInstance) {
        SqlExceptionWrapper sqlExceptionWrapper = this.exceptionAdvice.adviseSqlException(sqlException);
        this.connectionCleanup.reviewException(delegatedInstance, sqlExceptionWrapper);
        return sqlExceptionWrapper;
    }
    public SQLClientInfoException handleSQLClientInfoExcpetion(SQLClientInfoException sqlClientInfoException, ConnectionMember_I delegatedInstance) {
        SqlClientInfoExceptionWrapper sqlClientInfoExceptionWrapper = this.exceptionAdvice.adviseSQLClientInfoException(sqlClientInfoException);
        this.connectionCleanup.reviewException(delegatedInstance, sqlClientInfoExceptionWrapper);
        return sqlClientInfoExceptionWrapper;
    }
    public IOException handleIoException(IOException ioException, ConnectionMember_I delegatedInstance) {
        IoExceptionWrapper ioExceptionWrapper = this.exceptionAdvice.adviseIoException(ioException);
        this.connectionCleanup.reviewException(delegatedInstance, ioExceptionWrapper);
        return  ioExceptionWrapper;
    }
    public Exception handleException(Exception exception, ConnectionMember_I delegatedInstance) {
        ExceptionWrapper exceptionWrapper = this.exceptionAdvice.adviseException(exception);
        this.connectionCleanup.reviewException(delegatedInstance, exceptionWrapper);
        return exceptionWrapper;
    }

    public final UUID getConnectionId() {
        return this.connectionId;
    }

    public final CC getConnectionCleanup() {
        return this.connectionCleanup;
    }

    public ConnectionMember_I wrap(Object delegate, Class delegateClassReturned, ConnectionMember_I parent, Object[] args) {
        ConnectionMember_I wrapped = delegation.getDelegate(this, delegate, delegateClassReturned, args);
        this.connectionCleanup.add(wrapped, delegate, parent);
        return wrapped;
    }
}
