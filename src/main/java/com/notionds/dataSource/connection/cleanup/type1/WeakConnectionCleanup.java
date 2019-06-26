package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.connection.VendorConnection;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakConnectionCleanup<VC extends VendorConnection> extends WeakReference<ConnectionCleanupT1> {

    private final VC vendorConnection;

    public WeakConnectionCleanup(ConnectionCleanupT1 connectionCleanup, VC vendorConnection, ReferenceQueue<ConnectionCleanupT1> connectionCleanupRQ) {
        super(connectionCleanup, connectionCleanupRQ);
        this.vendorConnection = vendorConnection;
    }

    public void cleanup() {
        this.vendorConnection.release();
    }
}
