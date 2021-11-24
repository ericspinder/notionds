package com.notionds.dataSupplier.delegation.reflection.aggregation;

import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.time.Instant;

import static com.notionds.dataSupplier.operational.StringOption.*;

public abstract class InvokeInterceptor<N,O extends Operational<N,W>, W extends Wrapper<N>, G extends InvokeAggregator> {

    public static class Default<N,O extends Operational<N,W>, W extends Wrapper<N>, G extends InvokeAggregator> extends InvokeInterceptor<N,O,W,G> {

        public Default(O options, Aggregation<O,G> aggregation) {
            super(options, aggregation);
        }
        @Override
        public InvokeAccounting startInvoke(Method m, Object[] args) {
            if (m.getName().matches(options.getString(Aggreation_Method_REGEX.getI18n()))) {
                return  aggregation.newInvokeAccounting();
            }
            return null;
        }

        @Override
        public void exception(NotionExceptionWrapper notionExceptionWrapper, String description, Method method, InvokeAccounting invokeAccounting) {
            aggregation.populateException(notionExceptionWrapper, description, method, invokeAccounting);
        }

        @Override
        public void endInvoke(Method m, String description, InvokeAccounting invokeAccounting) {
            invokeAccounting.setFinishTime(Instant.now());
            aggregation.populateExecution(m,  description, invokeAccounting);
        }
    }

    protected final O options;
    protected final Aggregation<O,G> aggregation;

    public InvokeInterceptor(O options, Aggregation<O,G> aggregation) {
        this.options = options;
        this.aggregation = aggregation;
    }
    abstract InvokeAccounting startInvoke(Method m, Object[] args);

    abstract void exception(NotionExceptionWrapper notionExceptionWrapper, String description, Method m, InvokeAccounting invokeAccounting);

    abstract void endInvoke(Method m, String description, InvokeAccounting invokeAccounting);

}
