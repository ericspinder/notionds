package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;

public abstract class PreparedStatementLogging<G extends InvokeAggregator, D> extends ObjectProxyLogging<G, D> {

    public static class Default<D> extends PreparedStatementLogging<InvokeAggregator.Default_intoLog, D> {

        public Default(String sql) {
            super(Options.DEFAULT_OPTIONS_INSTANCE, LoggingService.Default.INSTANCE, sql);
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
        public void exception(NotionExceptionWrapper notionExceptionWrapper, String description, Method m, InvokeAccounting invokeAccounting) {
            this.loggingService.populateException(notionExceptionWrapper, sql, m, invokeAccounting);
        }

        @Override
        public void endInvoke(Method m, String description, InvokeAccounting invokeAccounting) {
            this.loggingService.populateExecution(m, sql, invokeAccounting);
        }
    }
    protected final String sql;

    public PreparedStatementLogging(Options options, LoggingService<?,?,?,?,?> loggingService, String sql) {
        super(options, loggingService);
        this.sql = sql;
    }

    public String getSql() {
        return this.sql;
    }
}
