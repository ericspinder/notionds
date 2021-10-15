package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.SqlExceptionWrapper;

import java.lang.reflect.Method;
import java.util.UUID;

public abstract class PreparedStatementLogging<O extends Options, A extends InvokeAccounting, G extends InvokeAggregator<A, D>, L extends InvokeLibrary<?,A, G, ?, ?, ?>, D> extends ObjectProxyLogging<O, A, G, L, D> {

    public static class Default<D> extends PreparedStatementLogging<Options.Default, InvokeAccounting.Default, InvokeAggregator.Default_intoLog<D>, InvokeLibrary.Default, D> {

        public Default(UUID connectionId, String sql) {
            super(Options.DEFAULT_INSTANCE, connectionId, InvokeLibrary.DEFAULT_INSTANCE, sql);
        }

        @Override
        public InvokeAccounting.Default startInvoke(Method m, Object[] args) {
            if (m.getName().startsWith("execute")) {
                return this.invokeLibrary.newInvokeAccounting(this.connectionId);
            }
            else {
                return null;
            }
        }

        @Override
        public void exception(SqlExceptionWrapper sqlExceptionWrapper, Method method, String ) {

        }

        @Override
        public void endInvoke(Method m, Object[] args, InvokeAccounting.Default invokeAccounting) {
            if (m.getName().startsWith("execute") && invokeAccounting != null) {
                this.invokeLibrary.populateAggregator(m, sql, invokeAccounting);
            }
        }
    }
    protected final String sql;

    public PreparedStatementLogging(O options, UUID connectionId, L invokeLibrary, String sql) {
        super(options, connectionId, invokeLibrary);
        this.sql = sql;
    }

    public String getSql() {
        return this.sql;
    }
}
