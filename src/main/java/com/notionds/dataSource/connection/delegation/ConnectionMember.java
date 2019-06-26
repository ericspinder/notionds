package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.ConnectionContainer;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class ConnectionMember implements ConnectionMember_I {

    protected final Object delegate;
    protected final ConnectionContainer connectionContainer;
    protected boolean isClosed = false;

    public ConnectionMember(ConnectionContainer connectionContainer, Object delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    public void closeDelegate() {
        this.isClosed = true;
        connectionContainer.getConnectionCleanup().cleanup(this);
    }

    /**
     * Handles the exception, then rethrows the 'real' exception
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

    public boolean isClosed() {
        return this.isClosed;
    }

}
