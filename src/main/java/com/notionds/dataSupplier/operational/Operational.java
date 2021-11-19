package com.notionds.dataSupplier.operational;

import com.notionds.dataSupplier.Controller;
import com.notionds.dataSupplier.NotionStartupException;
import com.notionds.dataSupplier.delegation.Wrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.ref.SoftReference;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

public abstract class Operational<N,W extends Wrapper<N>> {

    private static final Logger logger = LogManager.getLogger(Operational.class);
    public static final Operational.Default DEFAULT_OPTIONS_INSTANCE = new Default();


    public interface Option<V> {
        String getI18n();
        V getDefaultValue();
        String getDescription();
    }

    private SoftReference<Controller<N,?,W,?,?,?,?,?>> controllerSoftReference;
    protected StampedLock gate = new StampedLock();
    protected final Map<String, String> stringOptions = new HashMap<>();
    protected final Map<String, Integer> integerOptions = new HashMap<>();
    protected final Map<String, Duration> durationOptions = new HashMap<>();
    protected final Map<String, Boolean> booleanOptions = new HashMap<>();

    public static final class Default extends Operational {
        public Default() {
            super();
        }
    }
    public Operational() {
        this(Arrays.stream(Operational.class.getClass().getTypeParameters()).findFirst().get().getTypeName());
    }
    public Controller<N,?,W,?,?,?,?,?> getController() {
        long readLock = gate.readLock();
        try {
            return this.controllerSoftReference.get();
        }
        finally {
            gate.unlockRead(readLock);
        }
    }
    public void setController(Controller<N,?,W,?,?,?,?,?> controller) {
        long writeLock = gate.writeLock();
        try {
            this.controllerSoftReference = new SoftReference<>(controller);
        }
        finally {
            gate.unlockWrite(writeLock);
        }
    }

    public Operational(final String name) {
            this.setDefaultValues(DurationOption.values());
    }

    public Integer getInteger(String key) {
        if (this.integerOptions.containsKey(key)) {
            return this.integerOptions.get(key);
        }
        throw new NotionStartupException(NotionStartupException.Type.MissingDefaultValue, this.getClass());
    }
    public String getString(String key) {
        if (this.stringOptions.containsKey(key)) {
            return this.stringOptions.get(key);
        }
        throw new NotionStartupException(NotionStartupException.Type.MissingDefaultValue, this.getClass());
    }
    public Duration getDuration(String key) {
        if (this.durationOptions.containsKey(key)) {
            return (Duration) this.durationOptions.get(key);
        }
        throw new NotionStartupException(NotionStartupException.Type.MissingDefaultValue, this.getClass());
    }
    public Boolean getBoolean(String key) {
        if (this.booleanOptions.containsKey(key)) {
            return this.booleanOptions.get(key);
        }
        throw new NotionStartupException(NotionStartupException.Type.MissMatchedOptionKey, this.getClass());
    }
    public final <D> void setDefaultValues(Option<D>[] optionsLoad) {
        logger.info("loading default values");
        if (optionsLoad == null) return;
        long stamp = gate.writeLock();
        try {
            for (Option<D> option: optionsLoad) {
                if (option.getDefaultValue() instanceof String) {
                    this.stringOptions.put(option.getI18n(), (String) option.getDefaultValue());
                }
                else if (option.getDefaultValue() instanceof Integer) {
                    this.integerOptions.put(option.getI18n(), (Integer) option.getDefaultValue());
                }
                else if (option.getDefaultValue() instanceof Boolean) {
                    this.booleanOptions.put(option.getI18n(), (Boolean) option.getDefaultValue());
                }
                else if (option.getDefaultValue() instanceof Duration) {
                    this.durationOptions.put(option.getI18n(), (Duration) option.getDefaultValue());
                }
                else {
                    throw new NotionStartupException(NotionStartupException.Type.MissingDefaultValue, this.getClass());
                }
            }
        }
        finally {
            gate.unlockWrite(stamp);
        }
    }
    public final void put(String key, String stringValue) {
        this.stringOptions.put(key, stringValue);
    }
    public final void put(String key, Integer integerValue) {
        this.integerOptions.put(key, integerValue);
    }
    public final void put(String key, Boolean booleanValue) {
        this.booleanOptions.put(key, booleanValue);
    }
    public final void put(String key, Duration durationValue) {
        this.durationOptions.put(key, durationValue);
    }
}
