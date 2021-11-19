package com.notionds.dataSupplier.management;

public class NotionDsNotification extends javax.management.Notification {

    public static String ROLLOVER = "com.notionds.notification.rollover";

    public NotionDsNotification(String type, Object source, long sequenceNumber) {
        super(type, source, sequenceNumber);
    }

    public NotionDsNotification(String type, Object source, long sequenceNumber, String message) {
        super(type, source, sequenceNumber, message);
    }

    public NotionDsNotification(String type, Object source, long sequenceNumber, long timeStamp) {
        super(type, source, sequenceNumber, timeStamp);
    }

    public NotionDsNotification(String type, Object source, long sequenceNumber, long timeStamp, String message) {
        super(type, source, sequenceNumber, timeStamp, message);
    }
}
