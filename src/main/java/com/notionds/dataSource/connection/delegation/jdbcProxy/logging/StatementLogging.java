package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.util.UUID;


public class StatementLogging<O extends Options, G extends InvokeAggregator, D> extends ObjectProxyLogging<O, G, D> {

    public static class Default<D> extends StatementLogging<Options.Default, InvokeAggregator.Default_intoLog, D> {

        public Default(UUID connectionId) {
            super(Options.DEFAULT_OPTIONS_INSTANCE, connectionId, Analysis.DEFAULT_INSTANCE);
        }
    }

    public StatementLogging(O options, UUID connectionId, Analysis analysis) {
        super(options, connectionId, analysis);
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
        this.analysis.populateAggregator(notionExceptionWrapper, method, invokeAccounting);
    }

    @Override
    public void endInvoke(Method m, Object[] args, InvokeAccounting invokeAccounting) {
        if (m.getName().startsWith("execute")) {
            if (args.length > 0 && args[0] instanceof String) {
                this.analysis.populateAggregator(m, (String) args[0], invokeAccounting);
            }
            else {
                this.analysis.populateAggregator(m, "black swan", invokeAccounting);
            }
        }
    }


}
