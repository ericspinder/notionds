package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;

import java.lang.reflect.Method;
import java.time.Instant;


public class StatementLogging extends ObjectProxyLogging {

    public StatementLogging(LoggingService loggingService) {
        super(loggingService);
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
        invokeAccounting.setFinishTime(Instant.now());
        this.loggingService.populateExecution(m, description, invokeAccounting);
    }
}
