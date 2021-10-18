package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InvokeService<O extends Options, G extends InvokeAggregator, X extends ObjectProxyLogging<?,?,?>, S extends StatementLogging<?,?,?>, P extends PreparedStatementLogging<?,?,?>> {

    public static final Default DEFAULT_INSTANCE = new Default();

    public static class Default extends InvokeService<Options.Default, InvokeAggregator.Default_intoLog, ObjectProxyLogging.Default<?>, StatementLogging.Default<?>, PreparedStatementLogging.Default<?>> {

        public Default() {
            super(Options.DEFAULT_INSTANCE);
        }


        @Override
        protected <D> InvokeAggregator.Default_intoLog newInvokeAggregator(Method method, String description) {
            return new InvokeAggregator.Default_intoLog(method, description);
        }

        @Override
        protected String makeKey(Method method, String description) {
            if (description != null && !description.isBlank()) {
                return description;
            }
            else {
                return method.getDeclaringClass().getCanonicalName();
            }
        }

        @Override
        protected String makeKey(NotionExceptionWrapper notionExceptionWrapper) {
            StringBuilder key = new StringBuilder();
            key.append(notionExceptionWrapper.getMessage()).append(" : ").append(notionExceptionWrapper.getRecommendation());
            return key.toString();
        }

        @Override
        public InvokeAccounting newInvokeAccounting(UUID connectionId) {
            return new InvokeAccounting(connectionId);
        }

        @Override
        protected ObjectProxyLogging.Default newObjectProxyLogging(UUID connectionId) {
            return new ObjectProxyLogging.Default(connectionId);
        }

        @Override
        protected StatementLogging.Default newStatementLogging(UUID connectionId) {
            return new StatementLogging.Default(connectionId);
        }

        @Override
        protected PreparedStatementLogging.Default newPreparedStatementLogging(UUID connectionId, String sql) {
            return new PreparedStatementLogging.Default(connectionId, sql);
        }
    }

    protected final O options;
    protected final Map<String, G> invokeAggregators = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public InvokeService(O options) {
        this.options = options;
    }


    protected abstract <D> G newInvokeAggregator(Method method, String description);
    protected abstract String makeKey(Method method, String description);
    protected abstract String makeKey(NotionExceptionWrapper notionExceptionWrapper);
    public void populateAggregator(Method method, String description, InvokeAccounting invokeAccounting) {
        G ig = this.invokeAggregators.getOrDefault( makeKey(method, description), this.newInvokeAggregator(method, description));
        ig.addInvokeAccounting(invokeAccounting);
    }
    public void populateAggregator(NotionExceptionWrapper notionExceptionWrapper, Method method, InvokeAccounting invokeAccounting) {
        G ig = this.invokeAggregators.getOrDefault(makeKey(notionExceptionWrapper), this.newInvokeAggregator(method, ""));
        ig.addInvokeAccounting(invokeAccounting);
    }
    public  abstract InvokeAccounting newInvokeAccounting(UUID connectionId);
    protected abstract X newObjectProxyLogging(UUID connectionId);
    protected abstract S newStatementLogging(UUID connectionId);
    protected abstract P newPreparedStatementLogging(UUID connectionId, String sql);

}
