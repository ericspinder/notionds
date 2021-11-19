package com.notionds.dataSupplier.management;

import javax.management.Attribute;
import javax.management.MBeanAttributeInfo;
import java.util.function.Consumer;

public class WriteAttributeMBeanWrapper {

    private final Consumer<Attribute> consumer;
    private final MBeanAttributeInfo attributeInfo;

    public WriteAttributeMBeanWrapper(Consumer<Attribute> function, MBeanAttributeInfo attributeInfo) {
        this.consumer = function;
        this.attributeInfo = attributeInfo;
    }

    public Consumer<Attribute> getConsumer() {
        return this.consumer;
    }

    public MBeanAttributeInfo getAttributeInfo() {
        return attributeInfo;
    }
}
