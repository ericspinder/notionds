package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.UUID;


public class Logging_forStatement<O extends Options, IA extends InvokeAccounting, IG extends InvokeAggerator<O, IA>, IL extends InvokeLibrary<O, IA, IG>> extends Logging_forDbObject<O, IA, IG, IL> {

    public static class Default<O extends Options, IA extends InvokeAccounting, IG extends InvokeAggerator<O, IA>, IL extends InvokeLibrary<O, IA, IG>> extends Logging_forStatement<O, IA, IG, IL> {

        public Default(O options, UUID connectionId, IL invokeLibrary) {
            super(options, connectionId, invokeLibrary);
        }
    }
    private static final Logger logger = LoggerFactory.getLogger(Logging_forStatement.class);

    private InvokeAccounting currentTimer = null;

    public Logging_forStatement(O options, UUID connectionId, IL invokeLibrary) {
        super(options, connectionId, invokeLibrary);
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
        if (m.getName().startsWith("execute")) {
            if (args.length > 0 && args[0] instanceof String) {
                this.invokeLibrary.populateAggregator((String) args[0], invokeAccounting);
            }
            else {
                this.invokeLibrary.populateAggregator("No description", invokeAccounting);
            }
        }
    }


}
