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
    public ConnectionCleanupT1 register(VC vendorConnection) {
        try {
            ConnectionCleanupT1 connectionCleanup = new ConnectionCleanupT1(this.options, vendorConnection, this.connectionMemberRQ);
            return connectionCleanup;
        }
        catch (Exception e) {
            throw new RuntimeException("Problem creating ConnectionCleanup instance " + e.getMessage(), e);
        }
    }

    @Override
    protected void cleanup() throws InterruptedException {
        ConnectionMemberWR gcConnectionMember = (ConnectionWR) connectionMemberRQ.remove();
        gcConnectionMember.closeDelegate();

    }

    @Override
    protected  ConnectionCleanupT1 saveRegistration(ConnectionCleanupT1 connectionCleanup) {
        this.connectionCleanupMap.put(connectionCleanup, Instant.now());
        return connectionCleanup;
    }
}