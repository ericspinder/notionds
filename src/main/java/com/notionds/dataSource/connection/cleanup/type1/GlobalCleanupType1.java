package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.VendorConnection;
import com.notionds.dataSource.connection.cleanup.ConnectionCleanup;
import com.notionds.dataSource.connection.cleanup.GlobalCleanup;

import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.StampedLock;

public class GlobalCleanupType1<O extends Options, VC extends VendorConnection> extends GlobalCleanup<O, ConnectionCleanupType1<O, VC>, VC> {

    private Map<ConnectionCleanupType1, CCWeakReferenceType1> engagedVendorConnectionMap = new WeakHashMap<>();
    private ReferenceQueue<ConnectionCleanupType1> referenceQueue = new ReferenceQueue<>();

    public GlobalCleanupType1(O options) {
        super(options);
    }


    @Override
    public ConnectionCleanupType1 register(VC vendorConnection) {
        try {
            ConnectionCleanupType1 connectionCleanup = new ConnectionCleanupType1(this.options, vendorConnection);
            engagedVendorConnectionMap.put(connectionCleanup, new CCWeakReferenceType1(connectionCleanup, vendorConnection, this.referenceQueue));
            return connectionCleanup;
        }
        catch (Exception e) {
            throw new RuntimeException("Problem creating ConnectionCleanup instance " + e.getMessage(), e);
        }
    }
}
