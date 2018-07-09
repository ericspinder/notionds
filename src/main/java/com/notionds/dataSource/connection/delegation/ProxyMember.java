package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.ConnectionContainer;

import java.io.InputStream;
import java.io.Reader;
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
                this.closeDelegate();
                return Void.TYPE;
            case "isWrapperFor":
                return ((Class)args[0]).isInstance(delegate);
            case "unwrap":
                if (((Class)args[0]).isInstance(delegate)) {
                    return delegate;
                }
                return null;
            case "getConnectionContainer":
                return getConnectionContainer();
            case "getOperationAccounting":
                return getOperationAccounting();
            case "getConnection":
                return connectionContainer.getNotionConnection();
        }
        if (m.getReturnType().equals(Void.TYPE)) {
            try {
                m.invoke(delegate, args);
            }
            catch (InvocationTargetException ite) {
                this.throwCause(ite.getCause());
                throw ite;
            }
            return Void.TYPE;
        }
        if (m.getReturnType().isPrimitive()) {
            try {
                return m.invoke(delegate, args);
            }
            catch (InvocationTargetException ite) {
                this.throwCause(ite.getCause());
                throw ite;
            }
        }
        else if ((m.getReturnType().getPackage().getName().equals("javax.sql") || m.getReturnType().getPackage().getName().equals("java.sql"))
                && m.getReturnType().isInterface()) {

            try {
                return connectionContainer.wrap(m.invoke(delegate, args), m.getReturnType(), this);
            }
            catch(InvocationTargetException ite) {
                this.throwCause(ite.getCause());
                throw ite;
            }
        }
        else if (m.getReturnType().getCanonicalName().equals("java.io.InputStream")) {

            try {
                return new InputStreamDelegate(connectionContainer, (InputStream) m.invoke(delegate, args));
            }
            catch (InvocationTargetException ite) {
                this.throwCause(ite.getCause());
                throw ite;
            }
        }
        else if (m.getReturnType().getCanonicalName().equals("java.io.Reader")) {

            try {
                return new ReaderDelegate(connectionContainer, (Reader) m.invoke(delegate, args));
            }
            catch (InvocationTargetException ite) {
                this.throwCause(ite.getCause());
                throw ite;
            }
        }
        return null;
    }
}
