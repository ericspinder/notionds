package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.manual9.InputStreamDelegate;
import com.notionds.dataSource.connection.manual9.ReaderDelegate;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;

public abstract class ConnectionMember implements ConnectionMember_I {

    protected final Object delegate;
    protected final ConnectionContainer connectionContainer;
    private final OperationAccounting operationAccounting;

    public ConnectionMember(ConnectionContainer connectionContainer, Object delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
        this.operationAccounting = new OperationAccounting(connectionContainer.getConnectionId());
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    public void closeDelegate() throws Exception {
        if (delegate instanceof AutoCloseable) {
            connectionContainer.closeSqlException(this);
            return;
        }
        if (delegate instanceof Clob || delegate instanceof Blob) {
            connectionContainer.closeFree(this);
            return;
        }
        connectionContainer.closeNotNeeded(this);
        return;
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
