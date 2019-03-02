package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.logging.DbObjectLogging;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class ConnectionMember<L extends DbObjectLogging> implements ConnectionMember_I<L> {

    protected final Object delegate;
    protected final ConnectionContainer connectionContainer;
    private final L objectAccounting;

    public ConnectionMember(ConnectionContainer connectionContainer, Object delegate, L objectAccounting) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
        this.objectAccounting = objectAccounting;
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    public L getDbObjectLogging() {
        return this.objectAccounting;
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
