package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.SqlExceptionWrapper;

import java.lang.reflect.Method;
import java.util.UUID;


public class StatementLogging<O extends Options, A extends InvokeAccounting, G extends InvokeAggregator<A,D>, L extends InvokeLibrary<?,A,?,?,?,?>, D> extends ObjectProxyLogging<O, A, G, D> {

    public static class Default<D> extends StatementLogging<Options.Default, InvokeAccounting.Default, InvokeAggregator.Default_intoLog<D>, InvokeLibrary.Default, D> {

        public Default(UUID connectionId) {
            super(Options.DEFAULT_INSTANCE, connectionId, InvokeLibrary.DEFAULT_INSTANCE);
        }
    }

    public StatementLogging(O options, UUID connectionId, L invokeLibrary) {
        super(options, connectionId, invokeLibrary);
    }

    @Override
    public A startInvoke(Method m, Object[] args) {
        if (m.getName().startsWith("execute")) {
            return (A) this.invokeLibrary.newInvokeAccounting(this.connectionId);
        }
        else {
            return null;
        }
    }

    @Override
    public void exception(SqlExceptionWrapper sqlExceptionWrapper, Method method, A invokeAccounting) {
        this.invokeLibrary.populateAggregator(sqlExceptionWrapper, method, invokeAccounting);
    }

    @Override
    public void endInvoke(Method m, Object[] args, A invokeAccounting) {
        if (m.getName().startsWith("execute")) {
            if (args.length > 0 && args[0] instanceof String) {
                this.invokeLibrary.populateAggregator(m, (String) args[0], invokeAccounting);
            }
            else {
                this.invokeLibrary.populateAggregator(m, "unexplained", invokeAccounting);
            }
        }
    }


}
