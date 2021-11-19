package com.notionds.dataSupplier.management;

import javax.management.MBeanNotificationInfo;
import java.util.function.Supplier;

public class NotificationsMBeanWrapper<B> {

    private final Supplier<B> supplier;
    private final MBeanNotificationInfo notificationInfo;

    public NotificationsMBeanWrapper(Supplier<B> supplier, MBeanNotificationInfo notificationInfo) {
        this.supplier = supplier;
        this.notificationInfo = notificationInfo;
    }

    public Supplier<B> getSupplier() {
        return supplier;
    }

    public MBeanNotificationInfo getNotificationInfo() {
        return notificationInfo;
    }
}
