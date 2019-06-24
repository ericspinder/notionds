package com.notionds.dataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.StampedLock;

public abstract class Options {

    public final class Defaults extends Options {}
    public enum NotionDefaultStrings implements StringOption  {

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
        public String getDefaultValue() {
            return this.defaultValue;
        }
    }
    public enum NotionDefaultIntegers implements IntegerOption  {
        ConnectionAnalysis_Max_Exceptions("com.notion.connectionAnalysis.maxExceptions", "The maximum number of noncritical sql Exceptions before a connection will terminate", 5),
        ConnectionAnalysis_Max_Normal_Seconds("com.notion.connectionAnalysis.maxNormalSeconds", "The maximum time of an operation before it's reported as abnormal", 10),


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
        public Integer getDefaultValue() {
            return this.defaultValue;
        }
    }
    public enum NotionDefaultBooleans implements BooleanOption  {
        ConnectionContainer_Check_ResultSet("com.notion.connectionContainer.checkResultSet", "Order a check of all ResultSets before closing when cleanup() had not been called, until the connection had been closed", true),
        ConnectionPool_Use("com.notion.pool.usePool", "Should pool connections", true),
        Logging("com.notion.connection.delegation.proxyV1.logging.UseLogging", "Use ProxyV1 logging", false),
        LogNonExecuteProxyMembers("com.notion.connection.delegation.proxyV1.logging.LogNonExecuteProxyMembers", "Use a proxy wrapper for even non-execute proxy member classes, when logging is turned on", false),
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
        public Boolean getDefaultValue() {
            return this.defaultValue;
        }
    }

    public Options() {
        this.setDefaultStringValues(NotionDefaultStrings.values());
        this.setDefaultIntegerValues(NotionDefaultIntegers.values());
        this.setDefaultBooleanValues(NotionDefaultBooleans.values());
    }
    public Options(StringOption[] stringOptionsLoad, IntegerOption[] integerOptionsLoad, BooleanOption[] booleanOptionsLoad) {
        this();
        if (stringOptionsLoad != null) {
            this.setDefaultStringValues(stringOptionsLoad);
        }
        if (integerOptionsLoad != null) {
            this.setDefaultIntegerValues(integerOptionsLoad);
        }
        if (booleanOptionsLoad != null) {
            this.setDefaultBooleanValues(booleanOptionsLoad);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(Options.class);

    public interface Option {
        String getKey();
        String getDescription();
    }

    public interface StringOption extends Option {
        String getDefaultValue();
    }

    public interface IntegerOption extends Option {
        Integer getDefaultValue();
    }

    public interface BooleanOption extends Option {
        Boolean getDefaultValue();
    }

    protected StampedLock gate = new StampedLock();


    protected final Map<StringOption, String> stringOptionValues = new HashMap<>();
    protected final Map<IntegerOption, Integer> integerOptionValues = new HashMap<>();
    protected final Map<BooleanOption, Boolean> booleanOptionValues = new HashMap<>();
    protected final Map<String, Option> allOptions = new HashMap<>();


    public String getAllProperties() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key: allOptions.keySet()) {
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(get(key));
        }
        return stringBuilder.toString();
    }
    public String get(String key) {
        Option option = allOptions.get(key);
        if (option instanceof StringOption) {
            return this.get((StringOption) option);
        }
        if (option  instanceof IntegerOption) {
            return this.get((IntegerOption)option).toString();
        }
        if (option instanceof BooleanOption) {
            return this.get((BooleanOption) option).toString();
        }
        throw new RuntimeException("property not found: key= " + key);
    }
    public String get(StringOption stringOption) {
        long stamp = gate.readLock();
        try {
            return stringOptionValues.get(stringOption);
        }
        finally {
            gate.unlockWrite(stamp);
        }
    }
    public String get(Option anonymousStringOption) {
        long stamp = gate.readLock();
        try {
            return stringOptionValues.get(anonymousStringOption);
        }
        finally {
            gate.unlockWrite(stamp);
        }

    }
    public Integer get(IntegerOption integerOption) {
        long stamp = gate.readLock();
        try {
            return integerOptionValues.get(integerOption);
        }
        finally {
            gate.unlockWrite(stamp);
        }

    }
    public Boolean get(BooleanOption booleanOption) {
        long stamp = gate.readLock();
        try {
            return booleanOptionValues.get(booleanOption);
        }
        finally {
            gate.unlockWrite(stamp);
        }
    }

    public Option loadValue(String key, String value) {
        for (StringOption stringOption : stringOptionValues.keySet()) {
            if (stringOption.getKey().equals(key)) {
                stringOptionValues.put(stringOption, value);
                return stringOption;
            }
        }
        for (IntegerOption integerOption: integerOptionValues.keySet()) {
            if (integerOption.getKey().equals(key)) {
                try {
                    Integer integer = Integer.valueOf((String) value);
                    integerOptionValues.put(integerOption, integer);
                    return integerOption;
                } catch (NumberFormatException nfe) {
                    logger.error("problem trying to set key=" + key + " value= " + value + " allowing the default value (" + integerOption.getDefaultValue() + " to remain");
                    integerOptionValues.put(integerOption, integerOption.getDefaultValue());
                    return integerOption;
                }
            }
        }
        for (BooleanOption booleanOption: booleanOptionValues.keySet()) {
            if (booleanOption.getKey().equals(key)) {
                booleanOptionValues.put(booleanOption, Boolean.valueOf(value));
                return booleanOption;
            }
        }
        logger.error("Trying to set unknown value, key= " + key + " value= " + value + " loading as StringOption");
        StringOption stringOption = new StringOption() {
            @Override
            public String getDefaultValue() {
                return value;
            }

            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String getDescription() {
                return "key not previously registered";
            }
        };
        stringOptionValues.put(stringOption, value);
        allOptions.put(stringOption.getKey(), stringOption);
        return stringOption;
    }

    public final void loadProperties(Properties properties) {
        logger.info("loading properties");
        long stamp = gate.writeLock();
        try {
            for (Object propertyKeyObject : properties.keySet()) {
                String propertyKey = (String) propertyKeyObject;
                String propertyValue = properties.getProperty(propertyKey);
                loadValue(propertyKey, propertyValue);
            }
        }
        finally {
            gate.unlockWrite(stamp);
        }
    }
    public final void setDefaultStringValues(StringOption[] stringOptionsLoad) {
        logger.info("loading default values");
        long stamp = gate.writeLock();
        try {
            for (StringOption stringOption : stringOptionsLoad) {
                stringOptionValues.put(stringOption, stringOption.getDefaultValue());
                allOptions.put(stringOption.getKey(), stringOption);
            }
        }
        finally {
            gate.unlockWrite(stamp);
        }
    }
    public final void setDefaultIntegerValues(IntegerOption[] integerOptionsLoad) {
        logger.info("loading default values");
        long stamp = gate.writeLock();
        try {
            for (IntegerOption integerOption : integerOptionsLoad) {
                integerOptionValues.put(integerOption, integerOption.getDefaultValue());
                allOptions.put(integerOption.getKey(), integerOption);
            }
        }
        finally {
            gate.unlockWrite(stamp);
        }
    }
    public final void setDefaultBooleanValues(BooleanOption[] booleanOptionLoad) {
        logger.info("loading default values");
        long stamp = gate.writeLock();
        try {
            for (BooleanOption booleanOption : booleanOptionLoad) {
                booleanOptionValues.put(booleanOption, booleanOption.getDefaultValue());
                allOptions.put(booleanOption.getKey(), booleanOption);
            }
        }
        finally {
            gate.unlockWrite(stamp);
        }
    }
}
