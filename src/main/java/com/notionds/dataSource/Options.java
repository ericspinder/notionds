package com.notionds.dataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.StampedLock;

public abstract class Options {

    private static final Logger logger = LogManager.getLogger(Options.class);


    public interface Option<V> {
        String getKey();
        V getDefaultValue();
        String getDescription();
    }

    public enum Strings implements Option<String>  {
//        Management_JMX("com.notionds.jmx.management", "JMX management mBean Implementation", "com.notionds.dataSource.jmx.NotionDsBean"),
//        Logging_Method_REGEX("com.notionds.logging.method_regex", "The regex for the method or methods (how clever is your regex?) which need have an InvokeAccounting created", "^execute")
        ;
        private final String key;
        private final String description;
        private final String defaultValue;
        Strings(String key, String description, String defaultValue) {
            this.key = key;
            this.description = description;
            this.defaultValue = defaultValue;
        }
        public String getKey() {
            return key;
        }
        public String getDescription() {
            return this.description;
        }
        public String getDefaultValue() {
            return this.defaultValue;
        }
    }
    public enum Longs implements Option<Long> {

        ;
        private final String key;
        private final String description;
        private final Long defaultValue;
        Longs(String key, String description, long defaultValue) {
            this.key = key;
            this.description = description;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getDescription() {
            return description;
        }

        public Long getDefaultValue() {
            return defaultValue;
        }
    }
    public enum Integers implements Option<Integer>  {
        Advice_Exception_Aggregator_Map_Max_Size("com.notionds.advice.exception.aggregatorMap.maxSize", "The number of ", 1000),
        Advice_Nominal_Aggregator_Map_Max_Size("com.notionds.advice.nominal.aggregatorMap.maxSize", "The number of ", 1000),
        Connection_Max_Queue_Size("com.notionds.connection.Max_Queue_Size", "Max Connection Queue size", 50),
        Connections_Min_Active("com.notionds.connection.min_queue_size", "",5),
        Timeout_Retrieve_Connection("com.notionds.datasource.ConnectionPool.timeout_retrieve_connection","Login timeout in seconds", 10);
        ;
        private final String key;
        private final String description;
        private final Integer defaultValue;
        Integers(String key, String description, Integer defaultValue) {
            this.key = key;
            this.description = description;
            this.defaultValue = defaultValue;
        }
        public String getKey() {
            return key;
        }
        public String getDescription() {
            return this.description;
        }
        public Integer getDefaultValue() {
            return this.defaultValue;
        }
    }
    public enum Durations implements Option<Duration> {

        ConnectionTimeoutInPool("com.notionds.connections_timeout_in_pool", "Amount of time connections will wait in the pool before reaping excess of the number of active in pool connections", java.time.Duration.of(20, ChronoUnit.MINUTES)),
        ConnectionTimeoutInPool_Cool_Down("com.notionds.connections_timeout_in_pool_cool_down","Minimum amount of time between reaping extra active connections, this creates a walk down from the maximum number of connections", java.time.Duration.of(60, ChronoUnit.SECONDS)),
        ConnectionTimeoutOnLoan("com.notionds.connection_timeout_on_loan","Default max time before connection is automatically closed, breaking loaned connections", java.time.Duration.of(360, ChronoUnit.MINUTES)),
        ConnectionMaxLifetime("com.notionds.connection_timeout_max_lifetime","Max lifetime of a connection", java.time.Duration.of(2, ChronoUnit.HOURS)),
        ;
        private final String key;
        private final String description;
        private final java.time.Duration defaultValue;
        Durations(String key, String description, java.time.Duration defaultValue) {
            this.key = key;
            this.description = description;
            this.defaultValue = defaultValue;
        }
        public String getKey() {
            return this.key;
        }
        public String getDescription() {
            return this.description;
        }
        public java.time.Duration getDefaultValue() {
            return this.defaultValue;
        }
    }

    public enum Booleans implements Option<Boolean>  {

//        Logging("com.notion.connection.delegation.jdbcProxy.logging.UseLogging", "Use ProxyV1 logging", false),
//        LogNonExecuteProxyMembers("com.notion.connection.delegation.jdbcProxy.logging.LogNonExecuteProxyMembers", "Use a proxy wrapper for even non-execute proxy member classes, when logging is turned on", false),
        ;
        private final String key;
        private final String description;
        private final Boolean defaultValue;
        Booleans(String key, String description, Boolean defaultValue) {
            this.key = key;
            this.description = description;
            this.defaultValue = defaultValue;
        }
        public String getKey() {
            return key;
        }
        public String getDescription() {
            return this.description;
        }
        public Boolean getDefaultValue() {
            return this.defaultValue;
        }
    }

    protected StampedLock gate = new StampedLock();
    protected final Map<String,Object> allOptions = new HashMap<>();

    public static final class Default extends Options {
        public Default() {
            super(null);
        }
    }

    public Options(Properties overrideValues) {
        this.setOpeningValues(overrideValues, Strings.values(), Integers.values(), Longs.values(),Booleans.values(),Durations.values());
    }

    @SuppressWarnings("unchecked")
    public Object get(String key) {
        if (this.allOptions.containsKey(key)) {
            return this.allOptions.get(key);
        }
        throw new NotionStartupException(NotionStartupException.Type.MissingDefaultValue, Options.class);
    }
    private void setOpeningValues(Properties overrideProperties, Option<?>[]... defaultValues) {
        logger.info("loading default values");
        long stamp = gate.writeLock();
        try {
            for (Option<?>[] optionEnums: defaultValues) {
                for (Option<?> option: optionEnums) {
                    if (overrideProperties != null) {
                        Object overrideValue = overrideProperties.get(option.getKey());
                        if (overrideValue != null) {
                            allOptions.put(option.getKey(), overrideValue);
                            overrideProperties.remove(option.getKey());
                        } else {
                            allOptions.put(option.getKey(), option.getDefaultValue());
                        }
                    }
                    else {
                        allOptions.put(option.getKey(), option.getDefaultValue());
                    }
                }
            }
            if (overrideProperties != null && !overrideProperties.isEmpty()) {
                for (Map.Entry<Object, Object> entry: overrideProperties.entrySet()) {
                    this.allOptions.put((String) entry.getKey(),entry.getValue());
                }
            }
        }
        finally {
            gate.unlockWrite(stamp);
        }
        logger.info("finished   loading default values");
    }
    public void setValue(String key, Object value) {
        logger.info("Changing value for " + key);
        long stamp = gate.writeLock();
        try {
            this.allOptions.put(key,value);
        }
        finally {
            gate.unlockWrite(stamp);
        }
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        stringBuilder.append("Displaying all current default values:");
        stringBuilder.append('\n');
        for (String key: allOptions.keySet()) {
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(get(key).toString());
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }
}
