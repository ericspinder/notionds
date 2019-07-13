package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.VendorConnection;
import com.notionds.dataSource.connection.cleanup.GlobalCleanup;
import com.notionds.dataSource.connection.delegation.ConnectionMember;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;

import java.lang.ref.ReferenceQueue;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class GlobalCleanupT1<O extends Options, VC extends VendorConnection> extends GlobalCleanup<O, ConnectionCleanupT1<O, VC>, VC> {

    private ReferenceQueue<ConnectionMember_I> connectionMemberRQ = new ReferenceQueue<>();
    protected final Map<ConnectionCleanupT1, Instant> connectionCleanupMap = Collections.synchronizedMap(new WeakHashMap<>());

    public GlobalCleanupT1(O options) {
        super(options);
    }


    @Override
    protected void globalCleanup() throws InterruptedException {
        ConnectionMemberWR gcConnectionMember = (ConnectionWR) connectionMemberRQ.remove();
        if (gcConnectionMember.closeDelegate()) {

        }
        for (Map.Entry<ConnectionCleanupT1, Instant> connectionCleanupEntry: connectionCleanupMap.entrySet()) {
            Instant expireTime = connectionCleanupEntry.getValue();
            if (expireTime != null && expireTime.isAfter(Instant.now())) {
                ConnectionCleanupT1 connectionCleanupT1 = connectionCleanupEntry.getKey();
                connectionCleanupT1.cleanupAll();
            }
        }

    }

    @Override
    protected  ConnectionCleanupT1 saveRegistration(ConnectionCleanupT1 connectionCleanup, Instant expireInstant) {
        this.connectionCleanupMap.put(connectionCleanup, expireInstant);
        return connectionCleanup;
    }
}