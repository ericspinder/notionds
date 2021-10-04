package com.notionds.dataSource.connection.cleanup;

import com.notionds.dataSource.connection.cleanup.ConnectionCleanup;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.sql.Connection;

public class ConnectionArtifactWR<D extends Object> extends WeakReference<ConnectionArtifact_I> {

    private final D delegate;

    public ConnectionArtifactWR(ConnectionArtifact_I connectionMember, ReferenceQueue<ConnectionArtifact_I> referenceQueue, D delegate) {
        super(connectionMember, referenceQueue);
        this.delegate = delegate;
    }
    public boolean closeIfNotReturned() {
        if (this.delegate instanceof Connection) {
            return false;
        }
        else {
            ConnectionCleanup.DoDelegateClose(this.delegate);
            return true;
        }
    }

    public

}
