package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;


public class StatementLogging<G extends InvokeAggregator, D> extends ObjectProxyLogging<G, D> {

    public static class Default<D> extends StatementLogging<InvokeAggregator.Default_intoLog, D> {

        public Default() {
            super(Options.DEFAULT_OPTIONS_INSTANCE, LoggingService.Default.INSTANCE);
        }
    }

    public StatementLogging(Options options, LoggingService<?,?,?,?,?> loggingService) {
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
