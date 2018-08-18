package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.accounting.OperationAccounting;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class ConnectionMember implements ConnectionMember_I {

    protected final Object delegate;
    protected final ConnectionContainer connectionContainer;
    private final OperationAccounting operationAccounting;

    public ConnectionMember(ConnectionContainer connectionContainer, Object delegate, OperationAccounting operationAccounting) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
        this.operationAccounting = operationAccounting;
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    public void closeDelegate() throws Exception {
        connectionContainer.getConnectionCleanup().close(this);
    }



    /**
     * Handles the exception, if parameter is null then return and throw the 'real' exception
     * @param cause
     * @throws Throwable
     */
    protected void throwCause(Throwable cause) throws Throwable {
        if (cause != null) {
            if (cause instanceof SQLClientInfoException) {
                connectionContainer.handleSQLClientInfoExcpetion((SQLClientInfoException) cause, this);
            } else if (cause instanceof SQLException) {
                connectionContainer.handleSQLException((SQLException) cause, this);
            } else if (cause instanceof IOException) {
                connectionContainer.handleIoException((IOException) cause, this);
            } else if (cause instanceof Exception) {
                connectionContainer.handleException((Exception) cause, this);
            }
            throw cause;
        }
    }
}
