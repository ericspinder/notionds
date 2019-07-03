package com.notionds.dataSource.connection.cleanup;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.VendorConnection;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

public abstract class GlobalCleanup<O extends Options, CC extends ConnectionCleanup, VC extends VendorConnection> implements Runnable {

    protected final O options;
    protected final Class<CC> connectionCleanupClass;
    protected final Constructor<CC> connectionCleanupConstructor;
    protected boolean doCleanup = true;

    @SuppressWarnings("unchecked")
    public GlobalCleanup(O options) {
        this.options = options;
        try {
            this.connectionCleanupClass = (Class<CC>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            this.connectionCleanupConstructor = connectionCleanupClass.getDeclaredConstructor(Options.class, GlobalCleanup.class, VendorConnection.class);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException("problem creating ConnectionCleanup class", e);
        }
    }

    public CC register(VC vendorConnection) {
        try {
            return this.saveRegistration(this.connectionCleanupConstructor.newInstance(this.options, this, vendorConnection));
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("Problem creating ConnectionCleanup instance (" + connectionCleanupClass.getCanonicalName() + ") ", roe);
        }
    }

    protected abstract CC saveRegistration(CC connectionCleanup);

    protected abstract void cleanup() throws InterruptedException ;

    public void run() {
        try {
            while (doCleanup) {
                this.cleanup();
            }
        }
        catch (InterruptedException ie) {

        }
    }
}
