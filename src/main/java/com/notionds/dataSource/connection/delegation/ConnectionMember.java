package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.ConnectionMain;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class ConnectionMember implements ConnectionMember_I {

    protected final Object delegate;
    protected final ConnectionMain connectionMain;

    public ConnectionMember(ConnectionMain connectionMain, Object delegate) {
        this.connectionMain = connectionMain;
        this.delegate = delegate;
    }

    public ConnectionMain getConnectionMain() {
        return this.connectionMain;
    }

    public void closeDelegate() {
        connectionMain.getConnectionCleanup().cleanup(this, this.delegate);
    }

    /**
     * Handles the exception, then rethrows the 'real' exception
     * @param cause
     * @throws Throwable
     */
    protected void throwCause(Throwable cause) throws Throwable {
        if (cause != null) {
            if (cause instanceof SQLClientInfoException) {
                connectionMain.handleSQLClientInfoExcpetion((SQLClientInfoException) cause, this);
            } else if (cause instanceof SQLException) {
                connectionMain.handleSQLException((SQLException) cause, this);
            } else if (cause instanceof IOException) {
                connectionMain.handleIoException((IOException) cause, this);
            } else if (cause instanceof Exception) {
                connectionMain.handleException((Exception) cause, this);
            }
            throw cause;
        }
    }

}
