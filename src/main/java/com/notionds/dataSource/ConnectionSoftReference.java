package com.notionds.dataSource;

import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.sql.Connection;

public class ConnectionSoftReference extends SoftReference<ConnectionArtifact_I<Connection>> {

    private final SoftReference<ConnectionContainer> connectionContainerSoftReference;
    public ConnectionSoftReference(ConnectionArtifact_I<Connection> referent, ReferenceQueue<? super ConnectionArtifact_I<Connection>> q, ConnectionContainer connectionContainer) {
        super(referent, q);
        this.connectionContainerSoftReference = new SoftReference<>(connectionContainer);
    }

    public ConnectionContainer getConnectionContainer() {
        return connectionContainerSoftReference.get();
    }
}
