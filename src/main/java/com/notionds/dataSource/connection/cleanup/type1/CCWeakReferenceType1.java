package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.connection.VendorConnection;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class CCWeakReferenceType1<VC extends VendorConnection> extends WeakReference<ConnectionCleanupType1> {

    private final VC vendorConnection;
    private final
    public CCWeakReferenceType1(ConnectionCleanupType1 connectionCleanup, VC vendorConnection, ReferenceQueue referenceQueue) {
        super(connectionCleanup, referenceQueue);
        this.vendorConnection = vendorConnection;
    }

    public void cleanup() {
        this.vendorConnection.release();
    }
}
