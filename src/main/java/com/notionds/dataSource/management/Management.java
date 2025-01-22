package com.notionds.dataSource.management;

import com.notionds.dataSource.ConnectionPool;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.NotionStartupException;
import com.notionds.dataSource.Options;

import javax.management.*;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Management {
    protected final Options options;
    protected Map<String, Supplier<?>> getterSupplierList = new HashMap<>();
    protected Map<String, Consumer<?>> setterConsumerList = new HashMap<>();
    protected MBeanInfo mBeanInfo;
    public Management(Options options, String instanceName) {
        this.options = options;
        this.mBeanInfo = this.initialize(instanceName);
    }
    protected MBeanInfo initialize(String instanceName) {
        /*
         * The max time a connection will be allowed to stay active.
         */
        Supplier<Duration> maxConnectionLifetime_getter = () -> (Duration) this.options.get(Options.Durations.ConnectionMaxLifetime.getKey());
        Consumer<Duration> maxConnectionLifetime_setter = (Duration maxConnectionLifetime) -> this.options.setValue(Options.Durations.ConnectionMaxLifetime.getKey(), maxConnectionLifetime);
        /*
         * Default timeout when loaned out
         */
        Supplier<Duration> timeOnLoan_getter = () -> (Duration) this.options.get(Options.Durations.ConnectionTimeoutOnLoan.getKey());
        Consumer<Duration> timeOnLoan_setter = (Duration timeoutOnLoan_default) -> this.options.setValue(Options.Durations.ConnectionTimeoutOnLoan.getKey(), timeoutOnLoan_default);
        /*
         * Duration split into TimeUnits for efficient use in the poll method
         */
        Supplier<Integer> connectionRetrieve_getter = () -> (Integer) this.options.get(Options.Integers.Connection_Max_Wait_On_Create.getKey());
        Consumer<Integer> connectionRetrieve_setter = (Integer connection_retrieve_millis) -> this.options.setValue(Options.Integers.Timeout_Retrieve_Connection.getKey(), connection_retrieve_millis);
        /*
         * Max number of connections allowed, this is not a hard limit
         */
        Supplier<Integer> maxTotalAllowedConnections_getter = () -> (Integer) this.options.get(Options.Integers.Connection_Max_Queue_Size.getKey());
        Consumer<Integer> maxTotalAllowedConnections_setter = (Integer maxTotalAllowedConnections) -> this.options.setValue(Options.Integers.Connection_Max_Queue_Size.getKey(), maxTotalAllowedConnections);
        /*
         * The number of connections the system will attempt to keep in absence of breaching the maximum
         */
        Supplier<Integer> minActiveConnections_getter = () -> (Integer) this.options.get(Options.Integers.Connections_Min_Active.getKey());
        Consumer<Integer> minActiveConnections_setter = (Integer minActiveConnections) -> this.options.setValue(Options.Integers.Connections_Min_Active.getKey(), minActiveConnections);
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

        return new MBeanInfo(instanceName, "Notion Data Source Management Interface", attributeList.toArray(new MBeanAttributeInfo[attributeList.size()]), null, null, null);
    }

    public MBeanInfo getMBeanInfo() {
        return mBeanInfo;
    }

}
