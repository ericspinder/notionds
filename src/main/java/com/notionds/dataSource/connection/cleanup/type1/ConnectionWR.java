package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.connection.delegation.ConnectionMember_I;

import java.lang.ref.ReferenceQueue;
import java.sql.Connection;
import java.util.Map;

public class ConnectionWR extends ConnectionMemberWR {

    private final Map<ConnectionMember_I, ConnectionMemberWR> allConnectionMemberWR;

    public ConnectionWR(ConnectionMember_I connectionCleanup, ReferenceQueue<ConnectionMember_I> referenceQueue, Connection delegate, Map<ConnectionMember_I, ConnectionMemberWR> allConnectionMemberWR) {
        super(connectionCleanup, referenceQueue, delegate);
        this.allConnectionMemberWR = allConnectionMemberWR;
    }

    @Override
    public boolean closeDelegate() {
        for (ConnectionMemberWR connectionMemberWR: allConnectionMemberWR.values())  {
            if (connectionMemberWR != null) {
                connectionMemberWR.closeDelegate();
            }
        }
        return false;
    }
}
