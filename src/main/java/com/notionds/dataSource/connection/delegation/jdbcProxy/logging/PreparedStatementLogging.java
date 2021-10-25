package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.util.UUID;

public abstract class PreparedStatementLogging<O extends Options, G extends InvokeAggregator, D> extends ObjectProxyLogging<O, G, D> {

    public static class Default<D> extends PreparedStatementLogging<Options.Default, InvokeAggregator.Default_intoLog, D> {

        public Default(UUID connectionId, String sql) {
            super(Options.DEFAULT_OPTIONS_INSTANCE, connectionId, Analysis.DEFAULT_INSTANCE, sql);
        }

        @Override
        public InvokeAccounting startInvoke(Method m, Object[] args) {
            if (m.getName().startsWith("execute")) {
                return this.analysis.newInvokeAccounting(this.connectionId);
            }
            else {
                return null;
            }
        }

        @Override
        public void exception(NotionExceptionWrapper notionExceptionWrapper, Method method, InvokeAccounting invokeAccounting) {

        }

        @Override
        public void endInvoke(Method m, Object[] args, InvokeAccounting invokeAccounting) {
            if (m.getName().startsWith("execute") && invokeAccounting != null) {
                this.analysis.populateAggregator(m, sql, invokeAccounting);
            }
        }
    }
    protected final String sql;

    public PreparedStatementLogging(O options, UUID connectionId, Analysis analysis, String sql) {
        super(options, connectionId, analysis);
        this.sql = sql;
    }

    public String getSql() {
        return this.sql;
    }
}
