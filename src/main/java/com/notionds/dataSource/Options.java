package com.notionds.dataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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
        this.setDefaultStringValues(stringOptionsLoad);
        this.setDefaultIntegerValues(integerOptionsLoad);
        this.setDefaultBooleanValues(booleanOptionsLoad);
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


    public String get(StringOption stringOption) {
        long stamp = gate.readLock();
        try {
            return stringOptionValues.get(stringOption);
        }
        finally {
            gate.unlockWrite(stamp);
        }

    }
    public String get(String string) {
        long stamp = gate.readLock();
        try {
            return stringOptionValues.get(string);
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
            }
        }
        finally {
            gate.unlockWrite(stamp);
        }
    }
}
