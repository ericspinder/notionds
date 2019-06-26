package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.VendorConnection;
import com.notionds.dataSource.connection.cleanup.GlobalCleanup;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;

import java.lang.ref.ReferenceQueue;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class GlobalCleanupT1<O extends Options, VC extends VendorConnection> extends GlobalCleanup<O, ConnectionCleanupT1<O, VC>, VC> {

    private Map<ConnectionCleanupT1, WeakConnectionCleanup> allConnectionCleanupWR = Collections.synchronizedMap(new WeakHashMap<>());
    private ReferenceQueue<ConnectionCleanupT1> referenceQueue = new ReferenceQueue<>();

    public GlobalCleanupT1(O options) {
        super(options);
    }


    @Override
    public ConnectionCleanupT1 register(VC vendorConnection) {
        try {
            ConnectionCleanupT1 connectionCleanup = new ConnectionCleanupT1(this.options, vendorConnection, this);
            allConnectionCleanupWR.put(connectionCleanup, new WeakConnectionCleanup(connectionCleanup, vendorConnection, this.referenceQueue));
            return connectionCleanup;
        }
        catch (Exception e) {
            throw new RuntimeException("Problem creating ConnectionCleanup instance " + e.getMessage(), e);
        }
    }
}
