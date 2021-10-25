package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.EvictByLowCountMap;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.util.UUID;

public abstract class Analysis<O extends Options, G extends InvokeAggregator, X extends ObjectProxyLogging<?,?,?>, S extends StatementLogging<?,?,?>, P extends PreparedStatementLogging<?,?,?>> {

    public static final Default DEFAULT_INSTANCE = new Default();

    public static class Default extends Analysis<Options.Default, InvokeAggregator.Default_intoLog, ObjectProxyLogging.Default<?>, StatementLogging.Default<?>, PreparedStatementLogging.Default<?>> {

        public Default() {
            super(Options.DEFAULT_OPTIONS_INSTANCE);
        }


        @Override
        protected InvokeAggregator.Default_intoLog newInvokeAggregator(Method method, String description) {
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
    protected final EvictByLowCountMap<String, G> sqlExceptionAggregators;
    protected final EvictByLowCountMap<String, G> nominalOperationAggregators;

    @SuppressWarnings("unchecked")
    public Analysis(O options) {
        this.options = options;
        sqlExceptionAggregators = new EvictByLowCountMap<>((Integer) options.get(Options.NotionDefaultIntegers.Advice_Exception_Aggregator_Map_Max_Size.getKey()).getValue());
        nominalOperationAggregators = new EvictByLowCountMap<>((Integer) options.get(Options.NotionDefaultIntegers.Advice_Nominal_Aggregator_Map_Max_Size.getKey()).getValue());
    }

    public final EvictByLowCountMap getSqlExceptionAggregators() {
        return this.sqlExceptionAggregators;
    }
    public final EvictByLowCountMap getNominalOperationAggregators() {
        return this.nominalOperationAggregators;
    }


    protected abstract G newInvokeAggregator(Method method, String description);
    protected abstract String makeKey(Method method, String description);
    protected abstract String makeKey(NotionExceptionWrapper notionExceptionWrapper);
    public void populateAggregator(Method method, String description, InvokeAccounting invokeAccounting) {
        G ig = this.nominalOperationAggregators.getOrDefault( makeKey(method, description), this.newInvokeAggregator(method, description));
        ig.addInvokeAccounting(invokeAccounting);
    }
    public void populateAggregator(NotionExceptionWrapper notionExceptionWrapper, Method method, InvokeAccounting invokeAccounting) {
        G ig = this.sqlExceptionAggregators.getOrDefault(makeKey(notionExceptionWrapper), this.newInvokeAggregator(method, notionExceptionWrapper.getCause().getClass().getCanonicalName()));
        ig.addInvokeAccounting(invokeAccounting);
    }
    public  abstract InvokeAccounting newInvokeAccounting(UUID connectionId);
    protected abstract X newObjectProxyLogging(UUID connectionId);
    protected abstract S newStatementLogging(UUID connectionId);
    protected abstract P newPreparedStatementLogging(UUID connectionId, String sql);

}
