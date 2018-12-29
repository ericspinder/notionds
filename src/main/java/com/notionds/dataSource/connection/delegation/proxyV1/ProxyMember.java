package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.logging.DbObjectLogging;
import com.notionds.dataSource.connection.delegation.ConnectionMember;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyMember<O extends DbObjectLogging> extends ConnectionMember<O> implements InvocationHandler {

    public ProxyMember(ConnectionContainer connectionContainer, Object delegate, O operationAccounting) {
        super(connectionContainer, delegate, operationAccounting);
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        this.getDbObjectLogging().startInvoke(proxy, m, args);
        try {
            switch (m.getName()) {
                case "close":
                case "free":
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
                case "getDbObjectLogging":
                    return getDbObjectLogging();
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
                ConnectionMember_I connectionMember = connectionContainer.wrap(object, m.getReturnType(), this, (args != null && args[0] instanceof String) ? (String) args[0] : null);
                if (connectionMember != null) {
                    return connectionMember;
                }
                return object;
            } catch (InvocationTargetException ite) {
                this.throwCause(ite.getCause());
                throw ite;
            }
        }
        finally {
            this.getDbObjectLogging().endInvoke();
        }
    }
}
