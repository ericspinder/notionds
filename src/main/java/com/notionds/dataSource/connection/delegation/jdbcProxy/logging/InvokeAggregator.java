package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

public abstract class InvokeAggregator<IA extends InvokeAccounting, D> implements Comparable {

    public static class Default_intoLog<D> extends InvokeAggregator<InvokeAccounting.Default, D> {

        private static final Logger logger = LogManager.getLogger();

        public Default_intoLog(Method method, String message) {
            super(method, message);
        }

        public void addInvokeAccounting(InvokeAccounting.Default invokeAccounting) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.method.getDeclaringClass().getCanonicalName()).append(':').append(method).append('\n').append(invokeAccounting.toString());
            logger.info(stringBuilder.toString());
        }

        @Override
        public int compareTo(Object o) {
            return 0;
        }
    }

    protected final AtomicLong count = new AtomicLong();
    protected final Method method;
    protected final String description;

    public InvokeAggregator(Method method, String description) {
        this.method = method;
        this.description = description;
    }

    public long getCount() {
        return this.count.get();
    }

    public abstract void addInvokeAccounting(IA invokeAccounting);
}
