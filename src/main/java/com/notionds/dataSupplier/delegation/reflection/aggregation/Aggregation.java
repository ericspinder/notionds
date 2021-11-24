package com.notionds.dataSupplier.delegation.reflection.aggregation;

import com.notionds.dataSupplier.operational.IntegerOption;
import com.notionds.dataSupplier.pool.EvictByLowCountMap;
import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.util.Map;

public abstract class Aggregation<O extends Operational<?,?>, G extends InvokeAggregator> {

    public static class Default_Into_Log extends Aggregation<Operational.Default<?,?>, InvokeAggregator.Default_intoLog> {

        public Default_Into_Log(Method method, String message) {
            super(Operational.DEFAULT_OPTIONS_INSTANCE, new InvokeAggregator.Default_intoLog(method, message));
        }

        @Override
        protected InvokeAggregator.Default_intoLog newInvokeAggregator(Method method, String description) {
            return new InvokeAggregator.Default_intoLog(method, description);
        }

        @Override
        protected String makeKey(Method method, String description) {
            if (description != null && !description.isEmpty()) {
                return description.trim();
            }
            return method.getName();
        }

        @Override
        protected String makeKey(NotionExceptionWrapper notionExceptionWrapper, String description) {
            if (description != null && !description.isEmpty()) {
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

    }

    protected final O options;
    protected final G aggregator;
    protected final Map<String, G> sqlExceptionAggregators;
    protected final Map<String, G> nominalOperationAggregators;

    @SuppressWarnings("unchecked")
    public Aggregation(O options, G aggregator) {
        this.options = options;
        this.aggregator = aggregator;
        sqlExceptionAggregators = new EvictByLowCountMap<>(options.getInteger(IntegerOption.Advice_Exception_Aggregator_Map_Max_Size.getI18n()));
        nominalOperationAggregators = new EvictByLowCountMap<>(options.getInteger(IntegerOption.Advice_Nominal_Aggregator_Map_Max_Size.getI18n()));
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

}
