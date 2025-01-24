package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;

import java.lang.reflect.Method;


public class StatementLogging extends ObjectProxyLogging {

    public StatementLogging(Options options, LoggingService loggingService) {
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
    public void endInvoke(Method m, String description, InvokeAccounting invokeAccounting) {
        this.loggingService.populateExecution(m, description, invokeAccounting);
    }
}
