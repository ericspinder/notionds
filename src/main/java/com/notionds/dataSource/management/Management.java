package com.notionds.dataSource.management;

import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.NotionStartupException;
import com.notionds.dataSource.Options;

import javax.management.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Management<O extends Options> {

    public static class Default_Unavailable extends Management<Options.Default> {

        public static final Default_Unavailable INSTANCE = new Default_Unavailable();

        public Default_Unavailable() {
            super(Options.DEFAULT_OPTIONS_INSTANCE);
        }

        @Override
        protected void initialize(String instanceName) {

        }
    }

    public static class Default_JMX extends Management<Options.Default> {

        public static Default_JMX INSTANCE = new Default_JMX();

        private MBeanInfo mBeanInfo = null;
        public Default_JMX() {
            super(Options.DEFAULT_OPTIONS_INSTANCE);

        }

        @Override
        protected void initialize(String instanceName) {
            List<MBeanAttributeInfo> attributeList = new ArrayList<>();
            attributeList.add(new MBeanAttributeInfo("maxConnectionLifetime", "java.lang.Integer", "The max time a connection will be allowed to stay active.", true, true, false));
            this.getterSupplierList.put("maxConnectionLifetime", maxConnectionLifetime_getter);
            this.setterConsumerList.put("maxConnectionLifetime", maxConnectionLifetime_setter);
            attributeList.add(new MBeanAttributeInfo("timeOnLoan", "java.lang.Duration", "Default timeout when loaned out", true, true, false));
            this.getterSupplierList.put("timeOnLoan", timeOnLoan_getter);
            this.setterConsumerList.put("timeOnLoan", timeOnLoan_setter);
            attributeList.add(new MBeanAttributeInfo("connectionRetrieve", "java.lang.Duration", "Duration split into TimeUnits for efficient use in the poll method", true, true, false));
            this.getterSupplierList.put("connectionRetrieve", connectionRetrieve_getter);
            this.setterConsumerList.put("connectionRetrieve", connectionRetrieve_setter);
            attributeList.add(new MBeanAttributeInfo("maxTotalAllowedConnections", "java.lang.Integer", "Max number of connections allowed, this is not a hard limit", true, false, false));
            this.getterSupplierList.put("maxTotalAllowedConnections", maxTotalAllowedConnections_getter);
            this.setterConsumerList.put("maxTotalAllowedConnections", maxTotalAllowedConnections_setter);
            attributeList.add(new MBeanAttributeInfo("minActiveConnections", "java.lang.Integer", "The number of connections the system will attempt to keep in absence of breaching the maximum", true, false, false));
            this.getterSupplierList.put("minActiveConnections", minActiveConnections_getter);
            this.setterConsumerList.put("minActiveConnections", minActiveConnections_setter);

            mBeanInfo = new MBeanInfo(instanceName, "Notion Data Source Management Interface", attributeList.toArray(new MBeanAttributeInfo[attributeList.size()]), null, null, null);
        }

        public MBeanInfo getMBeanInfo() {
            if (mBeanInfo == null) {
                initialize(this.getClass().getCanonicalName());
            }
            return mBeanInfo;

        }
    }
    protected abstract void initialize(String instanceName);
    private final O options;
    private NotionDs<?,?,?,?> notionDs;
    protected Map<String, Supplier<?>> getterSupplierList = new HashMap<>();
    protected Map<String, Consumer<?>> setterConsumerList = new HashMap<>();
    protected Map<String, Function<?,?>> functionMap = new HashMap<>();

    /**
     * The max time a connection will be allowed to stay active.
     */
    protected Supplier<Duration> maxConnectionLifetime_getter = () -> notionDs.getConnectionPool().getMaxConnectionLifetime();
    protected Consumer<Duration> maxConnectionLifetime_setter = (Duration maxConnectionLifetime) -> notionDs.getConnectionPool().setMaxConnectionLifetime(maxConnectionLifetime);
    /**
     * Default timeout when loaned out
     */
    protected Supplier<Duration> timeOnLoan_getter = () -> notionDs.getConnectionPool().getTimeoutOnLoan_default();
    protected Consumer<Duration> timeOnLoan_setter = (Duration timeoutOnLoan_default) -> notionDs.getConnectionPool().setTimeoutOnLoan_default(timeoutOnLoan_default);
    /**
     * Duration split into TimeUnits for efficient use in the poll method
     */
    protected Supplier<Duration> connectionRetrieve_getter = () -> notionDs.getConnectionPool().getConnection_retrieveTimeout();
    protected Consumer<Duration> connectionRetrieve_setter = (Duration connection_retrieve_millis) -> notionDs.getConnectionPool().setConnection_retrieveTimeout(connection_retrieve_millis);
    /**
     * Max number of connections allowed, this is not a hard limit
     */
    protected Supplier<Integer> maxTotalAllowedConnections_getter = () -> notionDs.getConnectionPool().getMaxTotalAllowedConnections();
    protected Consumer<Integer> maxTotalAllowedConnections_setter = (Integer maxTotalAllowedConnections) -> notionDs.getConnectionPool().setMaxTotalAllowedConnections(maxTotalAllowedConnections);
    /**
     * The number of connections the system will attempt to keep in absence of breaching the maximum
     */
    protected Supplier<Integer> minActiveConnections_getter = () -> notionDs.getConnectionPool().getMinActiveConnections();
    protected Consumer<Integer> minActiveConnections_setter = (Integer minActiveConnections) -> notionDs.getConnectionPool().setMinActiveConnections(minActiveConnections);

    public Management(O options) {
        this.options = options;
    }

    public void setNotionDs(NotionDs<?,?,?,?> notionDs, String instanceName) {
        if (this.notionDs == null) {
            this.notionDs = notionDs;
            this.initialize(instanceName);
        }
        else {
            throw new NotionStartupException(NotionStartupException.Type.UnableToChangeJXM, this.getClass());
        }
    }
    public MBeanInfo getMBeanInfo() {
        throw new UnsupportedOperationException("MBeanInfo was not overridden for Management Bean");
    }
}
