package com.notionds.dataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

public abstract class Options {

    private static final Logger logger = LogManager.getLogger(Options.class);
    public static final Options.Default DEFAULT_INSTANCE = new Default();


    public interface Option<O> {
        String getKey();
        O getValue();
        String getDescription();
    }
    public enum NotionDefaultStrings implements Option<String>  {

        ;
        private final String key;
        private final String description;
        private final String defaultValue;
        NotionDefaultStrings(String key, String description, String defaultValue) {
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
        public String getValue() {
            return this.defaultValue;
        }
    }
    public enum NotionDefaultIntegers implements Option<Integer>  {
        ConnectionAnalysis_Max_Exceptions("com.notion.connectionAnalysis.maxExceptions", "The maximum number of noncritical sql Exceptions before a connection will terminate", 5),
        ConnectionAnalysis_Max_Normal_Seconds("com.notion.connectionAnalysis.maxNormalSeconds", "The maximum time of an operation before it's reported as abnormal", 10),
        Connection_Max_Wait_On_Create("com.notion.connection.max_weight_on_create", "The maximum amount of time in milliseconds until a RuntimeException is thrown to end", 1000),
        Connection_Max_Queue_Size("com.notion.connection.Max_Queue_Size", "Max Connection Queue size", 50),
        Connections_Min_Active("com.notion.connection.min_queue_size", "",10),
        ;
        private final String key;
        private final String description;
        private final Integer defaultValue;
        NotionDefaultIntegers(String key, String description, Integer defaultValue) {
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
        public Integer getValue() {
            return this.defaultValue;
        }
    }
    public enum NotionDefaultDuration implements Option<Duration> {
        ConnectionTimeoutInPool("com.notionds.connections_timeout_in_pool", "Amount of time connections will wait in the pool before reaping excess of the number of active in pool connections", Duration.of(20, ChronoUnit.MINUTES)),
        ConnectionTimeoutInPool_Cool_Down("com.notionds.connections_timeout_in_pool_cool_down","Minimum amount of time between reaping extra active connections, this creates a walk down from the maximum number of connections", Duration.of(1, ChronoUnit.MINUTES)),
        ConnectionTimeoutOnLoan("com.notionds.connection_timeout_on_loan","Default max time before connection is automatically closed, breaking loaned connections. Anything but a positive amount disables that function", Duration.of(3, ChronoUnit.MINUTES)),
        ConnectionMaxLifetime("com.notionds.connection_timeout_max_lifetime","Max lifetime of a connection", Duration.of(2, ChronoUnit.HOURS))
        ;
        private final String key;
        private final String description;
        private final Duration defaultValue;
        NotionDefaultDuration(String key, String description, Duration defaultValue) {
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
        public Duration getValue() {
            return this.defaultValue;
        }
    }
    public enum NotionDefaultBooleans implements Option<Boolean>  {

        ConnectionContainer_Check_ResultSet("com.notion.connectionMain.checkResultSet", "Order a check of all ResultSets before closing when cleanupAfterGC() had not been called, until the connection had been closed", true),
        ConnectionPool_Use("com.notion.pool.usePool", "Should pool connections", true),
        Logging("com.notion.connection.delegation.jdbcProxy.logging.UseLogging", "Use ProxyV1 logging", false),
        LogNonExecuteProxyMembers("com.notion.connection.delegation.jdbcProxy.logging.LogNonExecuteProxyMembers", "Use a proxy wrapper for even non-execute proxy member classes, when logging is turned on", false),
        ;
        private final String key;
        private final String description;
        private final Boolean defaultValue;
        NotionDefaultBooleans(String key, String description, Boolean defaultValue) {
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
        public Boolean getValue() {
            return this.defaultValue;
        }
    }

    protected StampedLock gate = new StampedLock();
    protected final Map<String, Option<?>> allOptions = new HashMap<>();

    public static final class Default extends Options {
        public Default() {
            super(null, null, null, null);
        }
    }

    public Options(Option<String>[] stringOptionsLoad, Option<Integer>[] integerOptionsLoad, Option<Boolean>[] booleanOptionsLoad, Option<Duration>[] durationOptionsLoad) {
        if (stringOptionsLoad != null) {
            this.setDefaultValues(stringOptionsLoad);
        }
        else {
            this.setDefaultValues(NotionDefaultStrings.values());
        }
        if (integerOptionsLoad != null) {
            this.setDefaultValues(integerOptionsLoad);
        }
        else {
            this.setDefaultValues(NotionDefaultIntegers.values());
        }
        if (booleanOptionsLoad != null) {
            this.setDefaultValues(booleanOptionsLoad);
        }
        else {
            this.setDefaultValues(NotionDefaultBooleans.values());
        }
        if (durationOptionsLoad != null) {
            this.setDefaultValues(durationOptionsLoad);
        }
        else {
            this.setDefaultValues(NotionDefaultDuration.values());
        }
    }

    @SuppressWarnings("unchecked")
    public <D> Option<D> get(String key) {
        if (this.allOptions.containsKey(key)) {
            try {
                return (Option<D>) this.allOptions.get(key);
            }
            catch(ClassCastException castException) {
                castException.printStackTrace();
                throw new NotionStartupException(NotionStartupException.Type.NullPointerOnGeneric, Options.class);
            }
        }
        throw new NotionStartupException(NotionStartupException.Type.MissingDefaultValue, Options.class);
    }
    public final <D> void setDefaultValues(Option<D>[] optionsLoad) {
        logger.info("loading default values");
        long stamp = gate.writeLock();
        try {
            for (Option<D> option: optionsLoad) {
                allOptions.put(option.getKey(), option);
            }
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
