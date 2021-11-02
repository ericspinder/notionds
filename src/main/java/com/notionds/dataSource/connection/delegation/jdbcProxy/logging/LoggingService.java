package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.EvictByLowCountMap;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class LoggingService<O extends Options, G extends InvokeAggregator, X extends ObjectProxyLogging<?,?,?>, S extends StatementLogging<?,?,?>, P extends PreparedStatementLogging<?,?,?>> {

    public static final Default DEFAULT_INSTANCE = new Default();

    public static class Default extends LoggingService<Options.Default, InvokeAggregator.Default_intoLog, ObjectProxyLogging.Default<?>, StatementLogging.Default<?>, PreparedStatementLogging.Default<?>> {

        public static final Logger log = LogManager.getLogger();

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
                return description.trim();
            }
            return method.getName();
        }

        @Override
        protected String makeKey(NotionExceptionWrapper notionExceptionWrapper, String description) {
            if (description != null && !description.isBlank()) {
                return description.trim();
            }
            StringBuilder key = new StringBuilder();
            key.append(notionExceptionWrapper.getMessage()).append(" : ").append(notionExceptionWrapper.getRecommendation());
            return key.toString().trim();
        }

        @Override
        public InvokeAccounting newInvokeAccounting() {
            return new InvokeAccounting();
        }

        @Override
        protected ObjectProxyLogging.Default newObjectProxyLogging() {
            return new ObjectProxyLogging.Default();
        }

        @Override
        protected StatementLogging.Default newStatementLogging() {
            return new StatementLogging.Default();
        }

        @Override
        protected PreparedStatementLogging.Default newPreparedStatementLogging(String sql) {
            return new PreparedStatementLogging.Default(sql);
        }
    }

    protected final O options;
    protected final Map<String, G> sqlExceptionAggregators;
    protected final Map<String, G> nominalOperationAggregators;

    @SuppressWarnings("unchecked")
    public LoggingService(O options) {
        this.options = options;
        sqlExceptionAggregators = new EvictByLowCountMap<>((Integer) options.get(Options.NotionDefaultIntegers.Advice_Exception_Aggregator_Map_Max_Size.getKey()).getValue());
        nominalOperationAggregators = new EvictByLowCountMap<>((Integer) options.get(Options.NotionDefaultIntegers.Advice_Nominal_Aggregator_Map_Max_Size.getKey()).getValue());
    }

    public final Map getSqlExceptionAggregators() {
        return this.sqlExceptionAggregators;
    }
    public final Map getNominalOperationAggregators() {
        return this.nominalOperationAggregators;
    }


    protected abstract G newInvokeAggregator(Method method, String description);
    protected abstract String makeKey(Method method, String description);
    protected abstract String makeKey(NotionExceptionWrapper notionExceptionWrapper, String description);
    public void populateExecution(Method method, String description, InvokeAccounting invokeAccounting) {
        String key = makeKey(method, description);
        G ig;
        if (this.nominalOperationAggregators.containsKey(key)) {
            ig = this.nominalOperationAggregators.get(key);
        }
        else {
            ig = this.newInvokeAggregator(method, description);
            this.nominalOperationAggregators.put(key, ig);
        }
        ig.addInvokeAccounting(invokeAccounting);
    }
    public void populateException(NotionExceptionWrapper notionExceptionWrapper, String description, Method method, InvokeAccounting invokeAccounting) {
        String key = makeKey(notionExceptionWrapper, description);
        G ig;
        if (this.sqlExceptionAggregators.containsKey(key)) {
            ig = this.sqlExceptionAggregators.get(key);
        }
        else {
            ig = this.newInvokeAggregator(method, description);
            this.sqlExceptionAggregators.put(key, ig);
        }
        ig.addInvokeAccounting(invokeAccounting);
    }
    public  abstract InvokeAccounting newInvokeAccounting();
    protected abstract X newObjectProxyLogging();
    protected abstract S newStatementLogging();
    protected abstract P newPreparedStatementLogging(String sql);

}
