package com.notionds.dataSource.management;

import javax.management.*;
import javax.management.modelmbean.*;
import java.awt.font.OpenType;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class NotionDescriptor implements Descriptor {

    private Map<Type, Object> typeMap = new HashMap<>();

    abstract boolean validate(Type fieldType) throws RuntimeOperationsException;

    @Override
    public Object getFieldValue(String fieldName) throws RuntimeOperationsException {
        return typeMap.get(Type.valueOf(fieldName));
    }

    @Override
    public void setField(String fieldName, Object fieldValue) throws RuntimeOperationsException {

    }
    public <T> void setField(Type fieldType, T fieldValue) throws RuntimeOperationsException {

    }

    @Override
    public String[] getFields() {
        return new String[0];
    }

    @Override
    public String[] getFieldNames() {
        return new String[0];
    }

    @Override
    public Object[] getFieldValues(String... fieldNames) {
        return new Object[0];
    }

    @Override
    public void removeField(String fieldName) {

    }

    @Override
    public void setFields(String[] fieldNames, Object[] fieldValues) throws RuntimeOperationsException {

    }

    @Override
    public Object clone() throws RuntimeOperationsException {
        return null;
    }

    @Override
    public boolean isValid() throws RuntimeOperationsException {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
    public static Consumer<String> regexValidator = (n) -> {

    };

    public enum Type {
        defaultValue(Object.class, Arrays.asList(MBeanAttributeInfo.class, MBeanParameterInfo.class), "Default value for an attribute or parameter. See javax.management.openmbean."),
        deprecated(String.class, Arrays.asList(MBeanFeatureInfo.class, MBeanInfo.class), "An indication that this element of the information model is no longer recommended for use. A set of MBeans defined by an application is collectively called an information model. The convention is for the value of this field to contain a string that is the version of the model in which the element was first deprecated, followed by a space, followed by an explanation of the deprecation, for example \"1.3 Replaced by the Capacity attribute\"."),
        descriptionResourceBundleBaseName(String.class, Arrays.asList(MBeanFeatureInfo.class, MBeanInfo.class), "The base name for the ResourceBundle in which the key given in the descriptionResourceKey field can be found, for example \"com.example.myapp.MBeanResources\". The meaning of this field is defined by this specification but the field is not set or used by the JMX API itself."),
        descriptionResourceKey(String.class,Arrays.asList(MBeanFeatureInfo.class, MBeanFeatureInfo.class), "A resource key for the description of this element. In conjunction with the descriptionResourceBundleBaseName, this can be used to find a localized version of the description. The meaning of this field is defined by this specification but the field is not set or used by the JMX API itself."),
        enabled(String.class, Arrays.asList(MBeanAttributeInfo.class, MBeanNotificationInfo.class, MBeanOperationInfo.class), "The string \"true\" or \"false\" according as this item is enabled. When an attribute or operation is not enabled, it exists but cannot currently be accessed. A user interface might present it as a greyed-out item. For example, an attribute might only be meaningful after the start() method of an MBean has been called, and is otherwise disabled. Likewise, a notification might be disabled if it cannot currently be emitted but could be in other circumstances."),
        exceptions_(String[].class, Arrays.asList(MBeanAttributeInfo.class, MBeanConstructorInfo.class, MBeanOperationInfo.class), "The class names of the exceptions that can be thrown when invoking a constructor or operation, or getting an attribute. The meaning of this field is defined by this specification but the field is not set or used by the JMX API itself. Exceptions thrown when setting an attribute are specified by the field setExceptions."),
        immutableInfo(String.class, Arrays.asList(MBeanInfo.class), "The string \"true\" or \"false\" according as this MBean's MBeanInfo is immutable. When this field is true, the MBeanInfo for the given MBean is guaranteed not to change over the lifetime of the MBean. Hence, a client can read it once and cache the read value. When this field is false or absent, there is no such guarantee, although that does not mean that the MBeanInfo will necessarily change. See also the \"jmx.mbean.info.changed\" notification."),
        infoTimeout(Long.class, Arrays.asList(MBeanInfo.class),	"The time in milli-seconds that the MBeanInfo can reasonably be expected to be unchanged. The value can be a Long or a decimal string. This provides a hint from a DynamicMBean or any MBean that does not define immutableInfo as true that the MBeanInfo is not likely to change within this period and therefore can be cached. When this field is missing or has the value zero, it is not recommended to cache the MBeanInfo unless it has the immutableInfo set to true or it has \"jmx.mbean.info.changed\" in its MBeanNotificationInfo array."),
        interfaceClassName(String.class, Arrays.asList(MBeanInfo.class), "The Java interface name for a Standard MBean or MXBean, as returned by Class.getName(). A Standard MBean or MXBean registered directly in the MBean Server or created using the StandardMBean class will have this field in its MBeanInfo Descriptor."),
        legalValues(Set .class, Arrays.asList(MBeanAttributeInfo.class, MBeanParameterInfo.class), "Legal values for an attribute or parameter. See javax.management.openmbean."),
        locale_(String.class, Arrays.asList(MBeanFeatureInfo.class, MBeanInfo.class), "The locale of the description in this MBeanInfo, MBeanAttributeInfo, etc, as returned by Locale.toString()."),
        maxValue(Object.class, Arrays.asList(MBeanAttributeInfo.class, MBeanParameterInfo.class), "Maximum legal value for an attribute or parameter. See javax.management.openmbean."),
        metricType(String.class, Arrays.asList(MBeanAttributeInfo.class, MBeanOperationInfo.class), "The type of a metric, one of the strings \"counter\" or \"gauge\". A metric is a measurement exported by an MBean, usually an attribute but sometimes the result of an operation. A metric that is a counter has a value that never decreases except by being reset to a starting value. Counter metrics are almost always non-negative integers. An example might be the number of requests received. A metric that is a gauge has a numeric value that can increase or decrease. Examples might be the number of open connections or a cache hit rate or a temperature reading."),
        minValue(Object.class, Arrays.asList(MBeanAttributeInfo.class, MBeanParameterInfo.class), "Minimum legal value for an attribute or parameter. See javax.management.openmbean."),
        mxbean(String.class, Arrays.asList(MBeanInfo.class),	"The string \"true\" or \"false\" according as this MBean is an MXBean. A Standard MBean or MXBean registered directly with the MBean Server or created using the StandardMBean class will have this field in its MBeanInfo Descriptor."),
        openType(OpenType.class, Arrays.asList(MBeanAttributeInfo.class, MBeanOperationInfo.class, MBeanParameterInfo.class), "The Open Type of this element. In the case of MBeanAttributeInfo and MBeanParameterInfo, this is the Open Type of the attribute or parameter. In the case of MBeanOperationInfo, it is the Open Type of the return value. This field is set in the Descriptor for all instances of OpenMBeanAttributeInfoSupport, OpenMBeanOperationInfoSupport, and OpenMBeanParameterInfoSupport. It is also set for attributes, operations, and parameters of MXBeans.\n This field can be set for an MBeanNotificationInfo, in which case it indicates the Open Type that the user data will have."),
        originalType(String.class, Arrays.asList(MBeanAttributeInfo.class, MBeanOperationInfo.class, MBeanParameterInfo.class), "The original Java type of this element as it appeared in the MXBean interface method that produced this MBeanAttributeInfo (etc). For example, a method\npublic MemoryUsage getHeapMemoryUsage();\nin an MXBean interface defines an attribute called HeapMemoryUsage of type CompositeData. The originalType field in the Descriptor for this attribute will have the value \"java.lang.management.MemoryUsage\".\nThe format of this string is described in the section Type Names of the MXBean specification."),
        setExceptions(String[].class, Arrays.asList(MBeanAttributeInfo.class), "The class names of the exceptions that can be thrown when setting an attribute. The meaning of this field is defined by this specification but the field is not set or used by the JMX API itself. Exceptions thrown when getting an attribute are specified by the field exceptions."),
        severity_(Integer.class, Arrays.asList(MBeanNotificationInfo.class), "The severity of this notification. It can be 0 to mean unknown severity or a value from 1 to 6 representing decreasing levels of severity. It can be represented as a decimal string or an Integer."),
        since(String.class, Arrays.asList(MBeanFeatureInfo.class, MBeanInfo.class),	"The version of the information model in which this element was introduced. A set of MBeans defined by an application is collectively called an information model. The application may also define versions of this model, and use the \"since\" field to record the version in which an element first appeared."),
        units(String.class, Arrays.asList(MBeanAttributeInfo.class, MBeanParameterInfo.class, MBeanOperationInfo.class), "The units in which an attribute, parameter, or operation return value is measured, for example \"bytes\" or \"seconds\"."),

        class_(String.class, Arrays.asList(ModelMBeanOperationInfo.class), "Class where method is defined (fully qualified)."),
        currencyTimeLimit(Number.class, Arrays.asList(ModelMBeanInfo .class, ModelMBeanAttributeInfo.class, ModelMBeanOperationInfo.class), "How long cached value is valid: <0 never, =0 always, >0 seconds."),
        default_(Object.class, Arrays.asList(ModelMBeanAttributeInfo.class), "Default value for attribute."),
        descriptorType(String.class, Arrays.asList(MBeanFeatureInfo.class, MBeanInfo.class), "Type of descriptor, \"mbean\", \"attribute\", \"constructor\", \"operation\",or \"notification\"."),
        displayName(String.class, Arrays.asList(MBeanFeatureInfo.class, MBeanInfo.class), "Human readable name of this item."),
        export(String.class, Arrays.asList(ModelMBeanInfo.class), "Name to be used to export/expose this MBean so that it is findable by other JMX Agents."),
        getMethod(String.class, Arrays.asList(ModelMBeanAttributeInfo.class), "Name of operation descriptor for get method."),
        lastUpdatedTimeStamp(Number.class, Arrays.asList(ModelMBeanAttributeInfo.class, ModelMBeanOperationInfo.class), "When value was set."),
        log(String.class, Arrays.asList(ModelMBeanInfo.class, ModelMBeanNotificationInfo .class), "t or T: log all notifications, f or F: log no notifications."),
        logFile(String.class, Arrays.asList(ModelMBeanInfo.class, ModelMBeanNotificationInfo.class),	"Fully qualified filename to log events to."),
        messageID(String.class, Arrays.asList(ModelMBeanNotificationInfo.class), "Unique key for message text (to allow translation, analysis)."),
        messageText	(String.class, Arrays.asList(ModelMBeanNotificationInfo.class), "Text of notification."),
        name(String.class, Arrays.asList(MBeanFeatureInfo.class, MBeanInfo.class), "Name of this item."),
        persistFile(String.class, Arrays.asList(ModelMBeanInfo.class), "File name into which the MBean should be persisted."),
        persistLocation(String.class, Arrays.asList(ModelMBeanInfo.class), "The fully qualified directory name where the MBean should be persisted (if appropriate)."),
        persistPeriod(Number.class, Arrays.asList(ModelMBeanInfo.class, ModelMBeanAttributeInfo.class), "Frequency of persist cycle in seconds. Used when persistPolicy is \"OnTimer\" or \"NoMoreOftenThan\"."),
        persistPolicy(String.class,	Arrays.asList(ModelMBeanInfo.class, ModelMBeanAttributeInfo.class), "One of: OnUpdate|OnTimer|NoMoreOftenThan|OnUnregister|Always|Never. See the section \"MBean Descriptor Fields\" in the JMX specification document."),
        presentationString(String.class, Arrays.asList(MBeanFeatureInfo.class, MBeanInfo.class), "XML formatted string to allow presentation of data."),
        protocolMap(Descriptor.class, Arrays.asList(ModelMBeanAttributeInfo.class), "See the section \"Protocol Map Support\" in the JMX specification document. Mappings must be appropriate for the attribute and entries can be updated or augmented at runtime."),
        role(String.class, Arrays.asList(ModelMBeanConstructorInfo .class, ModelMBeanOperationInfo.class), "One of \"constructor\", \"operation\", \"getter\", or \"setter\"."),
        setMethod(String.class, Arrays.asList(ModelMBeanAttributeInfo.class), "Name of operation descriptor for set method."),
        severity(Number.class, Arrays.asList(ModelMBeanNotificationInfo.class), "0-6 where 0: unknown; 1: non-recoverable; 2: critical, failure; 3: major, severe; 4: minor, marginal, error; 5: warning; 6: normal, cleared, informative"),
        targetObject(Object.class, Arrays.asList(ModelMBeanOperationInfo.class), "Object on which to execute this method."),
        targetType(String.class, Arrays.asList(ModelMBeanOperationInfo.class), "type of object reference for targetObject. Can be: ObjectReference | Handle | EJBHandle | IOR | RMIReference."),
        value(Object.class,	Arrays.asList(ModelMBeanAttributeInfo.class, ModelMBeanOperationInfo.class), "Current (cached) value for attribute or operation."),
        visibility(Number.class, Arrays.asList(MBeanFeatureInfo.class, MBeanInfo.class), "1-4 where 1: always visible, 4: rarely visible."),
        ;
        Class<?> clazz;
        List<Class<?>> featureInfos;
        String description;
        Type(Class<?> clazz, List<Class<?>> featureInfos, String description) {
            this.clazz = clazz;
            this.featureInfos = featureInfos;
            this.description = description;
        }

    }
}
