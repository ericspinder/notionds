package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ProxyConnectionArtifact;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;
import com.notionds.dataSource.exceptions.SqlExceptionWrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyWithLoggingConnectionArtifact<D, L extends ObjectProxyLogging> extends ProxyConnectionArtifact<D> implements InvocationHandler {

    private final L dbLogging;

    public ProxyWithLoggingConnectionArtifact(ConnectionContainer<?,?,?,?> connectionContainer, D delegate, L dbLogging) {
        super(connectionContainer, delegate);
        this.dbLogging = dbLogging;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        InvokeAccounting invokeAccounting = this.getDbLogging().startInvoke(m, args);
        try {
            switch (m.getName()) {
                case "getDbLogging":
                    return this.getDbLogging();
            }
            return super.invoke(proxy, m, args);
        }
        catch (Throwable throwable) {
            if (throwable instanceof NotionExceptionWrapper) {
                this.getDbLogging().exception((NotionExceptionWrapper) throwable, m, invokeAccounting);
            }
            throw throwable;
        }
        finally {
            this.getDbLogging().endInvoke(m, args, invokeAccounting);
        }
    }

    public L getDbLogging() {
        return this.dbLogging;
    }
}
