package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyMember extends ConnectionMember implements InvocationHandler {

    public ProxyMember(ConnectionContainer connectionContainer, Object delegate) {
        super(connectionContainer, delegate);
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
            case "getConnectionContainer":
                return getConnectionContainer();
            case "getConnection":
                return connectionContainer.getNotionConnection();
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
            ConnectionMember_I connectionMember = connectionContainer.wrap(object, m.getReturnType(), this, args);
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
}
