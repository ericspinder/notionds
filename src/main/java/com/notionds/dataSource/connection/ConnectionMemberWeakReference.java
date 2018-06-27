package com.notionds.dataSource.connection;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionMemberWeakReference<CM extends ConnectionMember_I> extends WeakReference<CM> {

    public enum State {
        Open("Open"),
        Closed("Closed"),
        Empty("Empty")
        ;
        final String description;
        State(String description) {
            this.description = description;
        }
    }
    private final NotionWrapper notionWrapper;
    private final ConcurrentHashMap children = new ConcurrentHashMap<ConnectionMemberWeakReference, State>();

    ConnectionMemberWeakReference(CM connectionMember, NotionWrapper notionWrapper, ConnectionMemberWeakReference<?> parent) {
        super(connectionMember, notionWrapper.getVendorConnection().getReferenceQueue());
        this.notionWrapper = notionWrapper;
    }

    public void addChild(ConnectionMemberWeakReference child) {
        this.children.put(child, State.Open);
    }

}
