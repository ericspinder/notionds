package com.notionds.dataSupplier.management;

import javax.management.Descriptor;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanFeatureInfo;
import java.util.function.Supplier;

public class ReadAttributeMBeanWrapper<R> {

    private final Supplier<R> supplier;
    private final MBeanAttributeInfo attributeInfo;

    public ReadAttributeMBeanWrapper(Supplier<R> supplier, MBeanAttributeInfo attributeInfo) {
        this.supplier = supplier;
        this.attributeInfo = attributeInfo;
    }

    public Supplier<R> getSupplier() {
        return supplier;
    }

    public MBeanAttributeInfo getAttributeInfo() {
        return attributeInfo;
    }
    public enum Type {
        name(String.class,new Class<?>[] {MBeanFeatureInfo.class}, "Attribute name"),
        descriptorType(String.class, "Must be \"attribute\"."),
        value(Object.class, "Current (cached) value for attribute."),
        default_(Object.class, "Default value for attribute."),
        displayName(String.class,"Name of attribute to be used in displays."),
        getMethod(String.class,	"Name of operation descriptor for get method."),
        setMethod(String.class,	"Name of operation descriptor for set method."),
        protocolMap(Descriptor.class, "See the section \"Protocol Map Support\" in the JMX specification document. Mappings must be appropriate for the attribute and entries can be updated or augmented at runtime."),
        persistPolicy(String.class,	"One of: OnUpdate|OnTimer|NoMoreOftenThan|OnUnregister|Always|Never. See the section \"MBean Descriptor Fields\" in the JMX specification document."),
        persistPeriod(Number.class, "Frequency of persist cycle in seconds. Used when persistPolicy is \"OnTimer\" or \"NoMoreOftenThan\"."),
        currencyTimeLimit(Number.class, "How long value is valid: <0 never, =0 always, >0 seconds."),
        lastUpdatedTimeStamp(Number.class, "When value was set."),
        visibility(Number.class, "1-4 where 1: always visible, 4: rarely visible."),
        presentationString(String.class, "XML formatted string to allow presentation of data.");
        Class<?> clazz;
        String description;
        Type(Class<?> clazz, String description) {
            this.clazz = clazz;
            this.description = description;
        }
        Type(Class<?> clazz, Class<?>[] mBeanFeatureInfos, String description) {
            this.clazz = clazz;
            this.description = description;
        }
    }
}
