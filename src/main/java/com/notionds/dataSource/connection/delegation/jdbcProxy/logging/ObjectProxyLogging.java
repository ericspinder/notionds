package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.SqlExceptionWrapper;

import java.lang.reflect.Method;
import java.util.UUID;

public abstract class ObjectProxyLogging<O extends Options, A extends InvokeAccounting, G extends InvokeAggregator<A, D>, L extends InvokeLibrary<?,A,?,?,?,?>, D> implements ProxyLogging_I<A> {

    public static class Default<D> extends ObjectProxyLogging<Options.Default, InvokeAccounting.Default, InvokeAggregator.Default_intoLog<D>, D> {

        public Default(final UUID connectionId) {
            super(Options.DEFAULT_INSTANCE, connectionId, InvokeLibrary.DEFAULT_INSTANCE);
        }
        @Override
        public InvokeAccounting.Default startInvoke(Method m, Object[] args) {
            return (InvokeAccounting.Default) invokeLibrary.newInvokeAccounting(connectionId);
        }

        @Override
        public void exception(SqlExceptionWrapper sqlExceptionWrapper, Method method, InvokeAccounting.Default invokeAccounting) {
            invokeLibrary.populateAggregator(sqlExceptionWrapper, invokeAccounting);
        }

        @Override
        public void endInvoke(Method m, Object[] args, InvokeAccounting.Default invokeAccounting) {
            invokeLibrary.populateAggregator(m,  "", invokeAccounting);
        }
    }

    protected final UUID connectionId;
    protected final O options;
    protected final InvokeLibrary invokeLibrary;

    public ObjectProxyLogging(O options, final UUID connectionId, InvokeLibrary invokeLibrary) {
        this.options = options;
        this.connectionId = connectionId;
        this.invokeLibrary = invokeLibrary;
    }

}
