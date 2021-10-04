package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public class ProxyConnectionArtifact implements InvocationHandler, ConnectionArtifact_I {

    protected final Object delegate;
    protected final ConnectionContainer connectionContainer;

    public ProxyConnectionArtifact(ConnectionContainer connectionContainer, Object delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
    }
    public ConnectionContainer getConnectionMain() {
        return this.connectionContainer;
    }

    public void closeDelegate() {
        connectionContainer.getConnectionCleanup().cleanup(this, this.delegate);
    }
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        switch (m.getName()) {
            case "close":
            case "free":
                this.handleClose();
                this.closeDelegate();
                return Void.TYPE;
            case "isWrapperFor":
                return ((Class) args[0]).isInstance(delegate);
            case "unwrap":
                if (((Class) args[0]).isInstance(delegate)) {
                    return delegate;
                }
                return null;
            case "getConnectionMain":
                return getConnectionMain();
            case "getConnection":
                return connectionContainer.getConnection();
        }
        if (m.getReturnType().equals(Void.TYPE)) {
            try {
                m.invoke(delegate, args);
            } catch (InvocationTargetException ite) {
                this.throwCause(ite.getCause());
                throw ite;
            }
            return Void.TYPE;
        }
        if (m.getReturnType().isPrimitive()) {
            try {
                return m.invoke(delegate, args);
            } catch (InvocationTargetException ite) {
                this.throwCause(ite.getCause());
                throw ite;
            }
        }
        try {
            Object object = m.invoke(delegate, args);
            String maybeSql = (args != null && args[0] instanceof String) ? (String) args[0] : null;
            ConnectionArtifact_I connectionMember = connectionContainer.wrap(object, m.getReturnType(), args);
            connectionContainer.
            if (connectionMember != null) {
                return connectionMember;
            }
            return object;
        } catch (InvocationTargetException ite) {
            this.throwCause(ite.getCause());
            throw ite;
        }
    }

    /**
     * For extending classes to have a callback on close
     */
    protected void handleClose() { }

    /**
     * Handles the exception, then rethrows the 'real' exception
     * @param cause
     * @throws Throwable
     */
    protected void throwCause(Throwable cause) throws Throwable {
        if (cause != null) {
            if (cause instanceof SQLClientInfoException) {
                connectionContainer.handleSQLClientInfoException((SQLClientInfoException) cause, this);
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
