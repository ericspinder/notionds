package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;

public class PreparedStatementLogging extends ObjectProxyLogging {

    protected final String sql;

    public PreparedStatementLogging(Options options, LoggingService loggingService, String sql) {
        super(options, loggingService);
        this.sql = sql;
    }
    @Override
    public void exception(NotionExceptionWrapper notionExceptionWrapper, String description, Method m, InvokeAccounting invokeAccounting) {
        this.loggingService.populateException(notionExceptionWrapper, sql, m, invokeAccounting);
    }

    @Override
    public void endInvoke(Method m, String description, InvokeAccounting invokeAccounting) {
        this.loggingService.populateExecution(m, sql, invokeAccounting);
    }
    public String getSql() {
        return this.sql;
    }
}
