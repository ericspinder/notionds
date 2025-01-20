package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.time.Instant;

import static com.notionds.dataSource.Options.NotionDefaultString.*;

public abstract class ObjectProxyLogging<G extends InvokeAggregator, D> {

    public static class Default<D> extends ObjectProxyLogging<InvokeAggregator.Default_intoLog, D> {

        public Default() {
            super(NotionDs.DEFAULT_OPTIONS_INSTANCE, LoggingService.Default.INSTANCE);
        }
        @Override
        public InvokeAccounting startInvoke(Method m, Object[] args) {
            if (m.getName().matches((String) options.get(Logging_Method_REGEX.getKey()).getValue())) {
                return  loggingService.newInvokeAccounting();
            }
            return null;
        }

        @Override
        public void exception(NotionExceptionWrapper notionExceptionWrapper, String description, Method method, InvokeAccounting invokeAccounting) {
            loggingService.populateException(notionExceptionWrapper, description, method, invokeAccounting);
        }

        @Override
        public void endInvoke(Method m, String description, InvokeAccounting invokeAccounting) {
            invokeAccounting.setFinishTime(Instant.now());
            loggingService.populateExecution(m,  description, invokeAccounting);
        }
    }

    protected final Options options;
    protected final LoggingService<?,?,?,?,?> loggingService;

    public ObjectProxyLogging(Options options, LoggingService<?,?,?,?,?> loggingService) {
        this.options = options;
        this.loggingService = loggingService;
    }
    abstract InvokeAccounting startInvoke(Method m, Object[] args);

    abstract void exception(NotionExceptionWrapper notionExceptionWrapper, String description, Method m, InvokeAccounting invokeAccounting);

    abstract void endInvoke(Method m, String description, InvokeAccounting invokeAccounting);

}
