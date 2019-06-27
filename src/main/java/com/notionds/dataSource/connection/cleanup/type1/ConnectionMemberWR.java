package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.connection.cleanup.ConnectionCleanup;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class ConnectionMemberWR extends WeakReference<ConnectionMember_I> {

    private final Object delegate;

    public ConnectionMemberWR(ConnectionMember_I connectionMember, ReferenceQueue<ConnectionMember_I> referenceQueue, Object delegate) {
        super(connectionMember, referenceQueue);
        this.delegate = delegate;
    }
    public void closeDelegate() {
        ConnectionCleanup.DoDelegateClose(this.delegate);
        this.clear();
    }
}
