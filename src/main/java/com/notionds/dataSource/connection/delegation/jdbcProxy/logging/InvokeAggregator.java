package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.EvictByLowCountMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

public class InvokeAggregator implements EvictByLowCountMap.EvictionByLowCountMember {

    private static final Logger logger = LogManager.getLogger();

    protected AtomicLong count = new AtomicLong();
    protected final Method method;
    protected final String description;

    public InvokeAggregator(Method method, String description) {
        this.method = method;
        this.description = description;
    }

    public void addInvokeAccounting(InvokeAccounting invokeAccounting) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\t").append(this.description).append(" : ");
        if (invokeAccounting.getDuration() != null) {
            stringBuilder.append("seconds = ").append(invokeAccounting.getDuration().getSeconds()).append('.').append(invokeAccounting.getDuration().getNano());
        }
        else stringBuilder.append("no duration available");
        stringBuilder.append(" count = ").append(count.incrementAndGet());
        logger.info(stringBuilder.toString());
    }
    public long getCount() {
        return this.count.get();
    }

}
