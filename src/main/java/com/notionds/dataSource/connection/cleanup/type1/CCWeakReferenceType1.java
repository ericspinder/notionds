package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.VendorConnection;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class CCWeakReferenceType1<VC extends VendorConnection> extends WeakReference<ConnectionCleanupType1> {

    private final VC vendorConnection;
    public CCWeakReferenceType1(ConnectionCleanupType1 connectionCleanup, VC vendorConnection, ReferenceQueue referenceQueue) {
        super(connectionCleanup, referenceQueue);
        this.vendorConnection = vendorConnection;
    }

    public VC getVendorConnection() {
        return this.vendorConnection;
    }


}
