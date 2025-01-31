package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.Closeable;
import java.lang.ref.Cleaner;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

public class ProxyConnectionArtifact<D> implements InvocationHandler, ConnectionArtifact_I<D> {

    protected final InnerState<D> innerState;
    protected final Cleaner.Cleanable cleanable;

    /**
     *
     * @param connectionContainer will be null if delegate is a 'top level' connection, must be not null otherwise
     * @param delegate the interface delegate to be wrapped
     */
    public ProxyConnectionArtifact(ConnectionContainer connectionContainer, D delegate) {
        this.innerState = new InnerState<>(delegate,connectionContainer);
        if (delegate instanceof Connection) {
            cleanable = CLEANER.register(this, innerState);
        }
        else {
            cleanable = null;
        }
    }


    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        switch (m.getName()) {
            case "close":
                if (this.getDelegate() instanceof Connection) {
                    this.getConnectionContainer().getConnectionPool().returnConnection((Connection) this.getDelegate(), this.getConnectionContainer(),"proxy close method");
                    return Void.TYPE;
                }
                else if (this.getDelegate() instanceof AutoCloseable) {
                    ((AutoCloseable) this.innerState.delegate()).close();
                }
                return Void.TYPE;
            case "free":
                if (this.getDelegate() instanceof Closeable) {
                    ((Closeable) this.getDelegate()).close();
                }
                return Void.TYPE;
            case "isWrapperFor":
                return ((Class<?>) args[0]).isInstance(getDelegate());
            case "unwrap":
                if (((Class<?>) args[0]).isInstance(getDelegate())) {
                    return getDelegate();
                }
                return null;
            case "getConnectionContainer":
                return getConnectionContainer();
            case "equals":
                return getDelegate().equals(args[0]);
        }
        if (m.getReturnType().equals(Void.TYPE)) {
            try {
                m.invoke(getDelegate(), args);
            } catch (InvocationTargetException ite) {
                this.getConnectionContainer().getConnectionPool().throwBackProcessedException(ite.getCause(), this);
                throw ite;
            }
            return Void.TYPE;
        }
        if (m.getReturnType().isPrimitive()) {
            try {
                return m.invoke(this.getDelegate(), args);
            } catch (InvocationTargetException ite) {
                this.getConnectionContainer().getConnectionPool().throwBackProcessedException(ite.getCause(),this);
                throw ite;
            }
        }
        try {
            Object object = m.invoke(getDelegate(), args);
            //String maybeSql = (args != null && args[0] instanceof String) ? (String) args[0] : null;
            return createNewConnectionArtifact(getConnectionContainer(),object, m.getReturnType(),args);

        } catch (InvocationTargetException ite) {
            this.getConnectionContainer().getConnectionPool().throwBackProcessedException(ite.getCause(),this);
            throw ite;
        }
    }
    public InnerState<D> getInnerState() {
        return this.innerState;
    }

    @SuppressWarnings("unchecked")
    private <T> T createNewConnectionArtifact(ConnectionContainer connectionContainer,T delegate,Class<?> delegateClass,Object... args) {
        return this.getConnectionContainer().getConnectionWrapper().getDelegate(connectionContainer,delegate,(Class<T>) delegateClass,args);
    }
}
