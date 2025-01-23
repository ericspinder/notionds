package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.time.Instant;
import java.util.UUID;

public class ProxyConnectionArtifact<D> implements InvocationHandler, ConnectionArtifact_I<D> {

    private static final Logger logger = LogManager.getLogger(ProxyConnectionArtifact.class);
    private final UUID uuid = UUID.randomUUID();
    private final Instant createTime = Instant.now();
    protected final D delegate;
    protected ConnectionContainer connectionContainer;

    /**
     *
     * @param connectionContainer will be null if delegate is a 'top level' connection, must be not null otherwise
     * @param delegate the interface delegate to be wrapped
     */
    public ProxyConnectionArtifact(ConnectionContainer connectionContainer, D delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
    }
    @Override
    public UUID getArtifactId() {
        return this.uuid;
    }
    @Override
    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }
    @Override
    public D getDelegate() {
        return delegate;
    }

    @Override
    public Instant getCreateInstant() {
        return createTime;
    }

    public void setConnectionContainer(ConnectionContainer connectionContainer) {
        if (this.connectionContainer == null) this.connectionContainer = connectionContainer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        logger.trace("ProxyConnectionArtifact - method = " + m.getName() + ", delegateClass: " + delegate.getClass());
        switch (m.getName()) {
            case "close":
                if (connectionContainer.get().equals(this)) {
                    this.connectionContainer.getConnectionPool().returnConnection((ConnectionArtifact_I<Connection>) this);
                    return Void.TYPE;
                }
                else if (this.delegate instanceof AutoCloseable) {
                    ((AutoCloseable) this.delegate).close();
                }
            case "free":
                if (this.delegate instanceof Closeable) {
                    ((Closeable) this.delegate).close();
                }
                return Void.TYPE;
            case "isWrapperFor":
                return ((Class<?>) args[0]).isInstance(delegate);
            case "unwrap":
                if (((Class<?>) args[0]).isInstance(delegate)) {
                    return delegate;
                }
                return null;
            case "getConnectionContainer":
                return getConnectionContainer();
            case "getArtifactId":
                return getArtifactId();
            case "equals":
                return equals(args[0]);
            case "setConnectionContainer":
                this.connectionContainer = (ConnectionContainer) args[0];
                return Void.TYPE;
        }
        if (m.getReturnType().equals(Void.TYPE)) {
            try {
                m.invoke(delegate, args);
            } catch (InvocationTargetException ite) {
                this.connectionContainer.getConnectionPool().throwBackProcessedException(ite.getCause(), this);
                throw ite;
            }
            return Void.TYPE;
        }
        if (m.getReturnType().isPrimitive()) {
            try {
                return m.invoke(delegate, args);
            } catch (InvocationTargetException ite) {
                this.getConnectionContainer().getConnectionPool().throwBackProcessedException(ite.getCause(),this);
                throw ite;
            }
        }
        try {
            Object object = m.invoke(delegate, args);
            //String maybeSql = (args != null && args[0] instanceof String) ? (String) args[0] : null;
            ConnectionArtifact_I<?> connectionMember = createNewConnectionArtifact(connectionContainer,object, m.getReturnType());
            if (connectionMember != null) {
                logger.trace("connectionMember = " + this.getArtifactId() + " class: " + m.getReturnType());
                return connectionMember;
            }
            return object;
        } catch (InvocationTargetException ite) {
            this.getConnectionContainer().getConnectionPool().throwBackProcessedException(ite.getCause(),this);
            throw ite;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> ConnectionArtifact_I<T> createNewConnectionArtifact(ConnectionContainer connectionContainer,T delegate,Class<?> delegateClass) {
        return this.connectionContainer.getConnectionWrapper().getDelegate(connectionContainer,delegate,(Class<T>) delegateClass);
    }
}
