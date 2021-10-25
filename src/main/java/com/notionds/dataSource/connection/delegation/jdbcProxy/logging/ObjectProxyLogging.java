package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.util.UUID;

public abstract class ObjectProxyLogging<O extends Options, G extends InvokeAggregator, D> {

    public static class Default<D> extends ObjectProxyLogging<Options.Default, InvokeAggregator.Default_intoLog, D> {

        public Default(final UUID connectionId) {
            super(Options.DEFAULT_OPTIONS_INSTANCE, connectionId, Analysis.DEFAULT_INSTANCE);
        }
        @Override
        public InvokeAccounting startInvoke(Method m, Object[] args) {
            return  analysis.newInvokeAccounting(connectionId);
        }

        @Override
        public void exception(NotionExceptionWrapper notionExceptionWrapper, Method method, InvokeAccounting invokeAccounting) {
            analysis.populateAggregator(notionExceptionWrapper, method, invokeAccounting);
        }

        @Override
        public void endInvoke(Method m, Object[] args, InvokeAccounting invokeAccounting) {
            analysis.populateAggregator(m,  "", invokeAccounting);
        }
    }

    protected final UUID connectionId;
    protected final O options;
    protected final Analysis analysis;

    public ObjectProxyLogging(O options, final UUID connectionId, Analysis analysis) {
        this.options = options;
        this.connectionId = connectionId;
        this.analysis = analysis;
    }
    abstract InvokeAccounting startInvoke(Method m, Object[] args);

    abstract void exception(NotionExceptionWrapper notionExceptionWrapper, Method m, InvokeAccounting invokeAccounting);

    abstract void endInvoke(Method m, Object[] args, InvokeAccounting invokeAccounting);

}
