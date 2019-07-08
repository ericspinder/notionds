package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public abstract class InvokeAggerator<O extends Options, IA extends InvokeAccounting> {

    public static class Default<O extends Options, IA extends InvokeAccounting> extends InvokeAggerator<O, IA> {

        private static final Logger logger = LoggerFactory.getLogger(Default.class);

        private boolean showCount = false;
        public Default(O options, String description) {
            super(options, description);
        }

        public void addInvokeAccounting(IA invokeAccounting) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("description=").append(this.getDescription());
            if (showCount) {
                stringBuilder.append(", count=").append(this.count.incrementAndGet());
            }
            stringBuilder.append(", ").append(invokeAccounting.toString());
            logger.info(stringBuilder.toString());
        }

        public void setShowCount(boolean showCount) {
            this.showCount = showCount;
        }
    }

    protected final AtomicLong count = new AtomicLong();
    protected O options;
    protected final String description;

    public InvokeAggerator(O options, String description) {
        this.description = description;
    }

    public String getDescription() {
        if (this.description != null && this.description.length()> 0) {
            return this.description;
        }
        else {
            return "No description";
        }
    }
    public long getCount() {
        return this.count.get();
    }

    public abstract void addInvokeAccounting(IA invokeAccounting);
}
