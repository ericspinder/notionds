package com.notionds.dataSupplier.delegation.refelction.logging;

import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.time.Instant;

import static com.notionds.dataSupplier.operational.StringOption.*;

public abstract class ObjectProxyLogging<O extends Operational, G extends InvokeAggregator, D> {

    public static class Default<D> extends ObjectProxyLogging<Operational.Default, InvokeAggregator.Default_intoLog, D> {

        public Default() {
            super(Operational.DEFAULT_OPTIONS_INSTANCE, LoggingService.DEFAULT_INSTANCE);
        }
        @Override
        public InvokeAccounting startInvoke(Method m, Object[] args) {
            if (m.getName().matches(options.getString(Logging_Method_REGEX.getI18n()))) {
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

    protected final O options;
    protected final LoggingService<?,?,?,?,?> loggingService;

    public ObjectProxyLogging(O options, LoggingService<?,?,?,?,?> loggingService) {
        this.options = options;
        this.loggingService = loggingService;
    }
    abstract InvokeAccounting startInvoke(Method m, Object[] args);

    abstract void exception(NotionExceptionWrapper notionExceptionWrapper, String description, Method m, InvokeAccounting invokeAccounting);

    abstract void endInvoke(Method m, String description, InvokeAccounting invokeAccounting);

}
