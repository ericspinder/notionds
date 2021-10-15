package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.connection.Cleanup;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.UUID;

public class ProxyConnectionArtifact<D> implements InvocationHandler, ConnectionArtifact_I {

    private UUID artifactId = UUID.randomUUID();
    protected final D delegate;
    protected final ConnectionContainer connectionContainer;

    public ProxyConnectionArtifact(ConnectionContainer<?,?,?,?> connectionContainer, D delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
    }
    @Override
    public UUID getArtifactId() {
        return this.artifactId;
    }
    @Override
    public ConnectionContainer<?,?,?,?> getConnectionMain() {
        return this.connectionContainer;
    }

    public void closeDelegate() {
        Cleanup.DoDelegateClose(this.delegate);
    }
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        switch (m.getName()) {
            case "close":
                if (connectionContainer.getConnection().equals(this)) {
                    this.connectionContainer.getCleanup().getConnectionPool().addConnectionFuture(this, true);
                    return Void.TYPE;
                }
            case "free":
                this.connectionContainer.closeChild(this);
                return Void.TYPE;
            case "isWrapperFor":
                return ((Class<?>) args[0]).isInstance(delegate);
            case "unwrap":
                if (((Class<?>) args[0]).isInstance(delegate)) {
                    return delegate;
                }
                return null;
            case "getConnectionMain":
                return getConnectionMain();
            case "getArtifactId":
                return getArtifactId();
            case "equals":
                return equals(args[0]);
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
    @Override
    public final boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (!(that instanceof ConnectionArtifact_I other)) {
            return false;
        }
        if (this.getArtifactId() == null) {
            if (other.getArtifactId() != null) {
                return false;
            }
        } else if (!this.getArtifactId().equals(other.getArtifactId())) {
            return false;
        }
        return true;
    }
}
