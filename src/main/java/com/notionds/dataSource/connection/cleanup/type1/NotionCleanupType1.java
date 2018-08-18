package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.VendorConnection;
import com.notionds.dataSource.connection.cleanup.ConnectionCleanup;
import com.notionds.dataSource.connection.cleanup.NotionCleanup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

public class NotionCleanupType1<O extends Options, CC extends ConnectionCleanup<O, ?, VC>, VC extends VendorConnection> extends NotionCleanup<O, CC, VC> {


    private ConnectionMemberWeakReferenceType1 connectionContainerWeakReference = null;
    private StampedLock weakWrapperCloseGate = new StampedLock();
    private Map<CCWeakReferenceType1, VC> engagedVendorConnectionMap = new HashMap<>();

    public NotionCleanupType1(O options) {
        super(options);
    }


    @Override
    public CC register(VC vendorConnection) {
        try {
            return this.connectionCleanupClass.getDeclaredConstructor(Options.class, NotionCleanup.class, vendorConnection.getClass()).newInstance(this.options, this, vendorConnection);
        }
        catch (Exception e) {
            throw new RuntimeException("Problem creating ConnectionCleanup instance " + e.getMessage(), e);
        }
    }
}
