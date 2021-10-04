package com.notionds.dataSource.connection.cleanup;

import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.lang.ref.ReferenceQueue;
import java.sql.Connection;
import java.util.Map;

public class ConnectionWR extends ConnectionArtifactWR<Connection> {

    private final Map<ConnectionArtifact_I, ConnectionArtifactWR> allConnectionMemberWR;

    public ConnectionWR(ConnectionArtifact_I connectionCleanup, ReferenceQueue<ConnectionArtifact_I> referenceQueue, Connection delegate, Map<ConnectionArtifact_I, ConnectionArtifactWR> allConnectionMemberWR) {
        super(connectionCleanup, referenceQueue, delegate);
        this.allConnectionMemberWR = allConnectionMemberWR;
    }

}
