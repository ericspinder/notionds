package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WeakConnectionMember extends WeakReference<ConnectionMember_I> {

    private final Object delegate;
    private final Map<WeakConnectionMember, Object> children = new ConcurrentHashMap<>

    public WeakConnectionMember(ConnectionMember_I connectionMember, Object delegate, ReferenceQueue referenceQueue) {
        super(connectionMember, referenceQueue);
        this.delegate = delegate;
    }

    public Object getDelegate() {
        return delegate;
    }

    public void addChild(WeakConnectionMember child) {
        this.children.put(child, State.Open);
    }

    public Map<WeakConnectionMember, Object> getChildren() {
        return this.children;
    }

}
