package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.EvictByLowCountMap;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.util.Map;

public class LoggingService {

    protected final Options options;
    protected final Map<String, InvokeAggregator> sqlExceptionAggregators;
    protected final Map<String, InvokeAggregator> nominalOperationAggregators;

    @SuppressWarnings("unchecked")
    public LoggingService(Options options) {
        this.options = options;
        sqlExceptionAggregators = new EvictByLowCountMap<>((Integer) options.get(Options.Integers.Advice_Exception_Aggregator_Map_Max_Size.getKey()));
        nominalOperationAggregators = new EvictByLowCountMap<>((Integer) options.get(Options.Integers.Advice_Nominal_Aggregator_Map_Max_Size.getKey()));
    }
    protected InvokeAggregator newInvokeAggregator(Method method, String description) {
        return new InvokeAggregator(method, description);
    }

    protected String makeKey(Method method, String description) {
        if (description != null && !description.isBlank()) {
            return description.trim();
        }
        return method.getName();
    }

    protected String makeKey(NotionExceptionWrapper notionExceptionWrapper, String description) {
        if (description != null && !description.isBlank()) {
            return description.trim();
        }
        StringBuilder key = new StringBuilder();
        key.append(notionExceptionWrapper.getMessage()).append(" : ").append(notionExceptionWrapper.getRecommendation());
        return key.toString().trim();
    }

    public InvokeAccounting newInvokeAccounting() {
        return new InvokeAccounting();
    }

    protected ObjectProxyLogging newObjectProxyLogging() {
        return new ObjectProxyLogging(this);
    }

    protected StatementLogging newStatementLogging() {
        return new StatementLogging(this);
    }

    protected PreparedStatementLogging newPreparedStatementLogging(String sql) {
        return new PreparedStatementLogging(this ,sql);
    }

    public final Map<String, InvokeAggregator> getSqlExceptionAggregators() {
        return this.sqlExceptionAggregators;
    }
    public final Map<String, InvokeAggregator> getNominalOperationAggregators() {
        return this.nominalOperationAggregators;
    }

    public void populateExecution(Method method, String description, InvokeAccounting invokeAccounting) {
        String key = makeKey(method, description);
        InvokeAggregator ig;
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
        InvokeAggregator ig;
        if (this.sqlExceptionAggregators.containsKey(key)) {
            ig = this.sqlExceptionAggregators.get(key);
        }
        else {
            ig = this.newInvokeAggregator(method, description);
            this.sqlExceptionAggregators.put(key, ig);
        }
        ig.addInvokeAccounting(invokeAccounting);
    }
}
