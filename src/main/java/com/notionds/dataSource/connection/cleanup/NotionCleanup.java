package com.notionds.dataSource.connection.cleanup;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.VendorConnection;

import java.lang.reflect.ParameterizedType;

public abstract class NotionCleanup<O extends Options, CC extends ConnectionCleanup, VC extends VendorConnection> {

    protected final O options;
    protected final Class<CC> connectionCleanupClass;

    @SuppressWarnings("unchecked")
    public NotionCleanup(O options) {
        this.options = options;
        try {
            this.connectionCleanupClass = (Class<CC>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        }
        catch (Exception e) {
            throw new RuntimeException("problem creating ConnectionCleanup class" + e);
        }
    }

    public abstract CC register(VC vendorConnection);

}