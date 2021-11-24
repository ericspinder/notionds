package com.notionds.dataSupplier;

import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.operational.Operational;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Factory<O extends Operational> {

    private Map<String, Controller<?,?,?,?,?,?,?,?,?>> controllerMap = new ConcurrentHashMap<>();

    public Factory(Operational<?,?> operational, LinkedBlockingDeque<NotionSupplier> notionSuppliers, Rubric rubric) {

    }
    public <N, W extends Wrapper<N>> void addNotionSupplier(LinkedBlockingDeque<NotionSupplier<N,?,W,?,?,?>> notionSuppliers) {

    }
    public <N, W extends Wrapper<N>> W wrap(N delegate, Class<N> delegateClass, Object[] args) {
        if (this.controllerMap.containsKey(delegateClass.getCanonicalName())) {
            Controller<?,?,?,?,?,?,?,?,?> controller = this.controllerMap.get(delegateClass.getCanonicalName());
            Container<N,O,W> newContainer = controller.notionSupplier.getNewContainer(controller);
            W wrapped = controller.getgetDelegate();
//            controller.pool.addNotion(wrapped, false);
            return wrapped;
        }
        throw new NotionStartupException(NotionStartupException.Type.SoftReference_Problem, this.getClass());
    }

    private static class Default_JMX {

            public static Default_JMX INSTANCE = new Default_JMX();

            protected Map<String, Supplier<?>> getterSupplierList = new HashMap<>();
            protected Map<String, Consumer<?>> setterConsumerList = new HashMap<>();
            protected Map<String, Function<?,?>> functionMap = new HashMap<>();

            private MBeanInfo mBeanInfo = null;
            public Default_JMX() {
                    super();

            }

//            @Override
//            protected void initialize(String instanceName) {
//                    List<MBeanAttributeInfo> attributeList = new ArrayList<>();
//                    /**
//                     * The max time a connection will be allowed to stay active.
//                     */
//                    attributeList.add(new MBeanAttributeInfo("maxConnectionLifetime", "java.lang.Integer", "The max time a connection will be allowed to stay active.", true, true, false));
//                    this.getterSupplierList.put("maxConnectionLifetime", maxConnectionLifetime_getter);
//                    this.setterConsumerList.put("maxConnectionLifetime", maxConnectionLifetime_setter);
//                    /**
//                     * Default timeout when loaned out
//                     */
//                    attributeList.add(new MBeanAttributeInfo("timeOnLoan", "java.lang.Duration", "Default timeout when loaned out", true, true, false));
//                    this.getterSupplierList.put("timeOnLoan", timeOnLoan_getter);
//                    this.setterConsumerList.put("timeOnLoan", timeOnLoan_setter);
//                    /**
//                     * Duration split into TimeUnits for efficient use in the poll method
//                     */
//                    attributeList.add(new MBeanAttributeInfo("connectionRetrieve", "java.lang.Duration", "Duration split into TimeUnits for efficient use in the poll method", true, true, false));
//                    this.getterSupplierList.put("connectionRetrieve", connectionRetrieve_getter);
//                    this.setterConsumerList.put("connectionRetrieve", connectionRetrieve_setter);
//                    attributeList.add(new MBeanAttributeInfo("maxTotalAllowedConnections", "java.lang.Integer", "Max number of connections allowed, this is not a hard limit", true, false, false));
//                    this.getterSupplierList.put("maxTotalAllowedConnections", maxTotalAllowedConnections_getter);
//                    this.setterConsumerList.put("maxTotalAllowedConnections", maxTotalAllowedConnections_setter);
//                    /**
//                     * The number of connections the system will attempt to keep in absence of breaching the maximum
//                     */
//                    attributeList.add(new MBeanAttributeInfo("minActiveConnections", "java.lang.Integer", "The number of connections the system will attempt to keep in absence of breaching the maximum", true, false, false));
//                    this.getterSupplierList.put("minActiveConnections", minActiveConnections_getter);
//                    this.setterConsumerList.put("minActiveConnections", minActiveConnections_setter);
//                    /**
//                     * Load the next
//                     */
//
//                    mBeanInfo = new MBeanInfo(instanceName, "Notion Data Source Management Interface", attributeList.toArray(new MBeanAttributeInfo[attributeList.size()]), null, null, null);
//            }
//
//            public MBeanInfo getMBeanInfo() {
//                    if (mBeanInfo == null) {
//                            initialize(this.getClass().getCanonicalName());
//                    }
//                    return mBeanInfo;
//
//            }
    }
}
