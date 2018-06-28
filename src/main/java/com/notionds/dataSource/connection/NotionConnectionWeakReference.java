package com.notionds.dataSource.connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NotionConnectionWeakReference<NW extends NotionConnection> extends NotionWeakReference<NW> {

    private final Map<NotionWeakReference, State> children = new ConcurrentHashMap<>();

    public NotionConnectionWeakReference(NW notionConnection, NotionWrapper notionWrapper) {
        super(notionConnection,notionWrapper);
    }

    public void addChild(NotionWeakReference child) {
        this.children.put(child, State.Open);
    }

    public Map<NotionWeakReference, State> getChildren() {
        return this.children;
    }
}
