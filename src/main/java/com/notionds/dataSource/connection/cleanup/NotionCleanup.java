package com.notionds.dataSource.connection.cleanup;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.VendorConnection;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

public abstract class NotionCleanup<O extends Options, CC extends ConnectionCleanup, VC extends VendorConnection> {

    protected final O options;
    protected final Class<CC> connectionCleanupClass;
    protected final Constructor<CC> connectionCleanupConstructor;

    @SuppressWarnings("unchecked")
    public NotionCleanup(O options) {
        this.options = options;
        try {
            this.connectionCleanupClass = (Class<CC>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            this.connectionCleanupConstructor = connectionCleanupClass.getDeclaredConstructor(Options.class, NotionCleanup.class, VendorConnection.class);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException("problem creating ConnectionCleanup class", e);
        }
    }

    public CC register(VC vendorConnection) {
        try {
            return this.connectionCleanupConstructor.newInstance(this.options, this, vendorConnection);
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("Problem creating ConnectionCleanup instance (" + connectionCleanupClass.getCanonicalName() + ") ", roe);
        }
    }

}
