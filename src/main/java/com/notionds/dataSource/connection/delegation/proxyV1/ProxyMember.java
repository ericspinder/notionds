package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.accounting.OperationAccounting;
import com.notionds.dataSource.connection.delegation.ConnectionMember;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Statement;

public class ProxyMember<O extends OperationAccounting> extends ConnectionMember<O> implements InvocationHandler {

    public ProxyMember(ConnectionContainer connectionContainer, Object delegate, O operationAccounting) {
        super(connectionContainer, delegate, operationAccounting);
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
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
            case "getOperationAccounting":
                return getOperationAccounting();
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
            ConnectionMember_I connectionMember = connectionContainer.wrap(object, m.getReturnType(), this, (args != null && args[0] instanceof String)?(String) args[0]:null);
            if (connectionMember != null) {
                return connectionMember;
            }
            return object;
        }
        catch(InvocationTargetException ite) {
            this.throwCause(ite.getCause());
            throw ite;
        }
    }
}
/*

        else if ((m.getReturnType().getPackage().getName().equals("javax.sql") || m.getReturnType().getPackage().getName().equals("java.sql"))
                && m.getReturnType().isInterface()) {

            try {
                if (args != null && m.getReturnType().isAssignableFrom(Statement.class) && args[0] instanceof String) {
                    return connectionContainer.wrap(m.invoke(delegate, args), m.getReturnType(), this, (String) args[0]);
                }
                return connectionContainer.wrap(m.invoke(delegate, args), m.getReturnType(), this, null);
            }
            catch(InvocationTargetException ite) {
                this.throwCause(ite.getCause());
                throw ite;
            }
        }
        else if (m.getReturnType().getCanonicalName().equals("java.io.InputStream")) {

            try {
                return new InputStreamDelegate(connectionContainer, (InputStream) m.invoke(delegate, args), new OperationAccounting(this.getOperationAccounting().getConnectionId()));
            }
            catch (InvocationTargetException ite) {
                this.throwCause(ite.getCause());
                throw ite;
            }
        }
        else if (m.getReturnType().getCanonicalName().equals("java.io.Reader")) {

            try {
                return new ReaderDelegate(connectionContainer, (Reader) m.invoke(delegate, args), new OperationAccounting(this.getOperationAccounting().getConnectionId()));
            }
            catch (InvocationTargetException ite) {
                this.throwCause(ite.getCause());
                throw ite;
            }
        }
        else if (m.getReturnType().getCanonicalName().equals("java.io.OutputStream")) {
            try {
                return new OutputStreamDelegate(connectionContainer, (OutputStream) m.invoke(delegate, args), new OperationAccounting(this.getOperationAccounting().getConnectionId()));
            }
            catch (InvocationTargetException ite) {
                this.throwCause(ite.getCause());
                throw ite;
            }
        }
        return null;*/
