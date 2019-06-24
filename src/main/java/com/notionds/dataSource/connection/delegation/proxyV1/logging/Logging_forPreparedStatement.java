package com.notionds.dataSource.connection.delegation.proxyV1.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.util.UUID;

public abstract class Logging_forPreparedStatement<O extends Options, IA extends InvokeAccounting, IG extends InvokeAggerator<O, IA>, IL extends InvokeLibrary<O, IA, IG>> extends Logging_forDbObject<O, IA, IG, IL> {

    public static class Default<O extends Options, IA extends InvokeAccounting, IG extends InvokeAggerator<O, IA>, IL extends InvokeLibrary<O, IA, IG>> extends  Logging_forPreparedStatement<O, IA, IG, IL> {

        public Default(O options, UUID connectionId, IL invokeLibrary, String sql) {
            super(options, connectionId, invokeLibrary, sql);
        }

        @Override
        public IA startInvoke(Method m, Object[] args) {
            if (m.getName().startsWith("execute")) {
                return this.invokeLibrary.createInvokeAccounting(this.connectionId);
            }
            else {
                return null;
            }
        }

        @Override
        public void exception(NotionExceptionWrapper notionExceptionWrapper, IA invokeAccounting) {

        }

        @Override
        public void endInvoke(Method m, Object[] args, IA invokeAccounting) {
            if (m.getName().startsWith("execute") && invokeAccounting != null) {
                this.invokeLibrary.populateAggregator(sql, invokeAccounting);
            }
        }
    }
    protected final String sql;

    public Logging_forPreparedStatement(O options, UUID connectionId, IL invokeLibrary, String sql) {
        super(options, connectionId, invokeLibrary);
        this.sql = sql;
    }

    public String getSql() {
        return this.sql;
    }
}
