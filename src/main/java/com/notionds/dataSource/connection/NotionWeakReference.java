package com.notionds.dataSource.connection;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

public class NotionWeakReference<CM extends ConnectionMember_I> extends WeakReference<CM> {

    public enum State {
        Open("Open, normal operations allowed, even encouraged one may say"),
        Closed("Closed, has already been closed"),
        Empty("Empty, allow current opertions to finish, but do not return to pool (if pooled)"),
        KillWithFire("Kill with fire, should have been destoryed")
        ;
        final String description;
        State(String description) {
            this.description = description;
        }
    }

    private final NotionWrapper notionWrapper;
    private State state;

    public NotionWeakReference(CM connectionMember, NotionWrapper notionWrapper) {
        super(connectionMember, notionWrapper.getVendorConnection().getReferenceQueue());
        this.notionWrapper = notionWrapper;
        this.state = State.Open;
    }

    public State getState() {
        return this.state;
    }


}
