package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.UUID;

public abstract class Logging_forDbObject<O extends Options, IA extends InvokeAccounting, IG extends InvokeAggerator<O, IA>, IL extends InvokeLibrary<O, IA, IG>> {

    public static class Default<O extends Options, IA extends InvokeAccounting, IG extends InvokeAggerator<O, IA>, IL extends InvokeLibrary<O, IA, IG>> extends Logging_forDbObject<O, IA, IG, IL> {

        private static final Logger logger = LoggerFactory.getLogger(Logging_forDbObject.Default.class);

        public Default(O options, final UUID connectionId, IL invokeLibrary) {
            super(options, connectionId, invokeLibrary);
        }
        @Override
        public IA startInvoke(Method m, Object[] args) {
            return null;
        }

        @Override
        public void exception(NotionExceptionWrapper notionExceptionWrapper, IA invokeAccounting) {

        }

        @Override
        public void endInvoke(Method m, Object[] args, IA invokeAccounting) {

        }
    }

    protected final UUID connectionId;
    protected final O options;
    protected final IL invokeLibrary;

    public Logging_forDbObject(O options, final UUID connectionId, IL invokeLibrary) {
        this.options = options;
        this.connectionId = connectionId;
        this.invokeLibrary = invokeLibrary;
    }

    public abstract IA startInvoke(Method m, Object[] args);
    public abstract void exception(NotionExceptionWrapper notionExceptionWrapper, IA invokeAccounting);
    public abstract void endInvoke(Method m, Object[] args, IA invokeAccounting);

}
