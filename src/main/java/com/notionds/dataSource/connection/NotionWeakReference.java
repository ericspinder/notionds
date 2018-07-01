package com.notionds.dataSource.connection;

import com.notionds.dataSource.connection.generator.ConnectionContainer;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NotionWeakReference extends WeakReference<ConnectionMember_I> {

    private final Object delegate;
    private final Map<NotionWeakReference, Object> children = new ConcurrentHashMap<>();

    public NotionWeakReference(ConnectionMember_I connectionMember, Object delegate) {
        super(connectionMember, connectionMember.getConnectionContainer().getReferenceQueue());
        this.delegate = delegate;
    }

    public Object getDelegate() {
        return delegate;
    }

    public void addChild(NotionWeakReference child) {
        this.children.put(child, State.Open);
    }

    public Map<NotionWeakReference, Object> getChildren() {
        return this.children;
    }

}
