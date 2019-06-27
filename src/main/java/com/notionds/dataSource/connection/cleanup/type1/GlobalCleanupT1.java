package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.VendorConnection;
import com.notionds.dataSource.connection.cleanup.GlobalCleanup;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class GlobalCleanupT1<O extends Options, VC extends VendorConnection> extends GlobalCleanup<O, ConnectionCleanupT1<O, VC>, VC> {

    private Map<ConnectionCleanupT1, WeakReference<ConnectionCleanupT1>> allConnectionCleanupWR = Collections.synchronizedMap(new WeakHashMap<>());
    private ReferenceQueue<ConnectionCleanupT1> collectionCleanupRQ = new ReferenceQueue<>();
    private ReferenceQueue<ConnectionMember_I> connectionMemberRQ = new ReferenceQueue<>();

    public GlobalCleanupT1(O options) {
        super(options);
    }


    @Override
    public ConnectionCleanupT1 register(VC vendorConnection) {
        try {
            Map<ConnectionMember_I, ConnectionMemberWR> allConnectionMemberWR = Collections.synchronizedMap(new WeakHashMap<>());
            ConnectionCleanupT1 connectionCleanup = new ConnectionCleanupT1(this.options, vendorConnection, this.connectionMemberRQ);
            allConnectionCleanupWR.put(connectionCleanup, new WeakReference<>(connectionCleanup, this.collectionCleanupRQ));
            return connectionCleanup;
        }
        catch (Exception e) {
            throw new RuntimeException("Problem creating ConnectionCleanup instance " + e.getMessage(), e);
        }
    }

    @Override
    protected void cleanup() throws InterruptedException {
        WeakReference<ConnectionCleanupT1> gcConnectionCleanup = (WeakReference<ConnectionCleanupT1>) collectionCleanupRQ.remove();

    }


}