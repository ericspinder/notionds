package com.notionds.dataSource.connection;

import java.lang.ref.WeakReference;

public class NotionWeakReference<CM extends ConnectionMember_I> extends WeakReference<CM> {

    private final NotionWrapper notionWrapper;

    public NotionWeakReference(CM connectionMember, NotionWrapper notionWrapper) {
        super(connectionMember, notionWrapper.getVendorConnection().getReferenceQueue());
        this.notionWrapper = notionWrapper;
    }
}
