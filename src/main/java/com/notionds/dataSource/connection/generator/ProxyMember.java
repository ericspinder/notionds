package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.manual9.InputStreamDelegate;
import com.notionds.dataSource.connection.manual9.ReaderDelegate;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Wrapper;

public class ProxyMember implements ConnectionMember_I, InvocationHandler {

    private final Object delegate;
    private final ConnectionContainer connectionContainer;
    private final OperationAccounting operationAccounting;

    public ProxyMember(ConnectionContainer connectionContainer, Object delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
        this.operationAccounting = new OperationAccounting(connectionContainer.getConnectionId());
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    public void close() throws Exception {
        if (delegate instanceof AutoCloseable) {
            connectionContainer.closeSqlException(this);
            return;
        }
        connectionContainer.closeFree(this);
        return;

    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        if (m.getReturnType().equals(void.class)) {
            if (m.getName().equals("close")) {
                connectionContainer.closeSqlException(this);
            }
            else if (m.getName().equals("free")) {
                connectionContainer.closeFree(this);
            }
            else {
                try {
                    m.invoke(delegate, args);
                }
                catch (InvocationTargetException ite) {
                    this.throwCause(ite);
                }
            }
            return Void.TYPE;
        }
        if (m.getReturnType().isPrimitive()) {
            if (m.getName().equals("isWrapperFor")) {
                return ((Class)args[0]).isInstance(delegate);
            }
            else if (m.getName().equals("unwrap") && ((Class)args[0]).isInstance(delegate)) {
                return delegate;
            }
            try {
                return m.invoke(delegate, args);
            }
            catch (InvocationTargetException ite) {
                this.throwCause(ite);
            }
        }
        else if ((m.getReturnType().getPackage().getName().equals("javax.sql") || m.getReturnType().getPackage().getName().equals("java.sql"))
                && m.getReturnType().isInterface()) {

            if (m.getReturnType().getCanonicalName().equals("java.sql.Connection")) {
                return connectionContainer.getNotionConnection();
            }
            try {
                return connectionContainer.wrap(m.invoke(delegate, args), m.getReturnType());
            }
            catch(InvocationTargetException ite) {
                this.throwCause(ite);
            }
        }
        else if (m.getReturnType().getCanonicalName().equals("java.io.InputStream")) {
            try {
                return new InputStreamDelegate(connectionContainer, (InputStream) m.invoke(delegate, args));
            }
            catch (InvocationTargetException ite) {
                this.throwCause(ite);
            }
        }
        else if (m.getReturnType().getCanonicalName().equals("java.io.Reader")) {
            try {
                return new ReaderDelegate(connectionContainer, (Reader) m.invoke(delegate, args));
            }
            catch (InvocationTargetException ite) {
                this.throwCause(ite);
            }
        }


        return null;
    }

    private void throwCause(Exception e) throws Throwable {
        Throwable cause = e.getCause();
        if (cause instanceof SQLClientInfoException) {
            connectionContainer.handleSQLClientInfoExcpetion((SQLClientInfoException) cause, this);
        }
        else if (cause instanceof SQLException) {
            connectionContainer.handleSQLException((SQLException)cause, this);
        }
        else if (cause instanceof IOException) {
            connectionContainer.handleIoException((IOException) cause, this);
        }
        else if (cause instanceof Exception) {
            connectionContainer.handleException((Exception)cause, this);
        }
    }
}
