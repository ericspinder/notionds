package com.notionds.dataSupplier.delegation.refelction.logging;

import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;


public class StatementLogging<O extends Operational, G extends InvokeAggregator, D> extends ObjectProxyLogging<O, G, D> {

    public static class Default<D> extends StatementLogging<Operational.Default, InvokeAggregator.Default_intoLog, D> {

        public Default() {
            super(Operational.DEFAULT_OPTIONS_INSTANCE, LoggingService.DEFAULT_INSTANCE);
        }
    }

    public StatementLogging(O options, LoggingService<?,?,?,?,?> loggingService) {
        super(options, loggingService);
    }

    @Override
    public InvokeAccounting startInvoke(Method m, Object[] args) {
        if (m.getName().startsWith("execute")) {
            return this.loggingService.newInvokeAccounting();
        }
        else {
            return null;
        }
    }

    @Override
    public void exception(NotionExceptionWrapper notionExceptionWrapper, String description, Method method, InvokeAccounting invokeAccounting) {
        this.loggingService.populateException(notionExceptionWrapper, description, method, invokeAccounting);
    }

    @Override
    public void endInvoke(Method m, String description, InvokeAccounting invokeAccounting) {
        this.loggingService.populateExecution(m, description, invokeAccounting);
    }
}
