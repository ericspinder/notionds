package com.notionds.dataSource.connection;

import java.util.concurrent.ConcurrentHashMap;

public class NotionConnectionWeakReference<NW extends NotionConnection> extends NotionWeakReference<NW> {

    private final ConcurrentHashMap children = new ConcurrentHashMap<NotionWeakReference, State>();

    public NotionConnectionWeakReference(NW notionConnection, NotionWrapper notionWrapper) {
        super(notionConnection,notionWrapper);
    }

    public void addChild(NotionWeakReference child) {
        this.children.put(child, State.Open);
    }
}
