package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.SqlExceptionWrapper;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InvokeLibrary<O extends Options, A extends InvokeAccounting, I extends InvokeAggregator<A, ?>, X extends ObjectProxyLogging<?,A,?,?,?>, S extends StatementLogging<?,A,?,?,?>, P extends PreparedStatementLogging<?,A,?,?,?>> {

    public static final Default DEFAULT_INSTANCE = new Default();

    public static class Default extends InvokeLibrary<Options.Default, InvokeAccounting.Default, InvokeAggregator.Default_intoLog<?>, ObjectProxyLogging.Default<?>, StatementLogging.Default<?>, PreparedStatementLogging.Default<?>> {

        public Default() {
            super(Options.DEFAULT_INSTANCE);
        }


        @Override
        protected <D> InvokeAggregator.Default_intoLog<InvokeAccounting.Default> newInvokeAggregator(Method method, String description) {
            return new InvokeAggregator.Default_intoLog<>(method, description);
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
        protected String makeKey(SqlExceptionWrapper sqlExceptionWrapper) {
            return sqlExceptionWrapper.getSQLState() + " : " + sqlExceptionWrapper.getMessage();
        }

        @Override
        public InvokeAccounting.Default newInvokeAccounting(UUID connectionId) {
            return new InvokeAccounting.Default(connectionId);
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
    protected final Map<String, I> invokeAggregators = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public InvokeLibrary(O options) {
        this.options = options;
    }


    protected abstract <D> I newInvokeAggregator(Method method, String description);
    protected abstract String makeKey(Method method, String description);
    protected abstract String makeKey(SqlExceptionWrapper sqlExceptionWrapper);
    public <D> void populateAggregator(Method method, String description, A invokeAccounting) {
        I ig = this.invokeAggregators.getOrDefault( makeKey(method, description), this.newInvokeAggregator(method, description));
        ig.addInvokeAccounting(invokeAccounting);
    }
    public void populateAggregator(SqlExceptionWrapper notionExceptionWrapper, Method method, A invokeAccounting) {
        I ig = this.invokeAggregators.getOrDefault(makeKey(notionExceptionWrapper), this.newInvokeAggregator(method, ""));
        ig.addInvokeAccounting(invokeAccounting);
    }

    public  abstract A newInvokeAccounting(UUID connectionId);
    protected abstract X newObjectProxyLogging(UUID connectionId);
    protected abstract S newStatementLogging(UUID connectionId);
    protected abstract P newPreparedStatementLogging(UUID connectionId, String sql);

}
