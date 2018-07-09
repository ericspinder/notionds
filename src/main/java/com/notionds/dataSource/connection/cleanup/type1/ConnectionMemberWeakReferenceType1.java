package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionMemberWeakReferenceType1 extends WeakReference<ConnectionMember_I> {

    private final Object delegate;
    private final Map<ConnectionMemberWeakReferenceType1, Object> children = new ConcurrentHashMap<>();

    public ConnectionMemberWeakReferenceType1(ConnectionMember_I connectionMember, Object delegate, ReferenceQueue referenceQueue) {
        super(connectionMember, referenceQueue);
        this.delegate = delegate;
    }

    public Object getDelegate() {
        return delegate;
    }

    public void addChild(ConnectionMemberWeakReferenceType1 child) {
        this.children.put(child, State.Open);
    }

    public Map<ConnectionMemberWeakReferenceType1, Object> getChildren() {
        return this.children;
    }

}
