package com.notionds.dataSource.connection;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;

public abstract class VendorConnection<CA extends ConnectionAnalysis> {

    private final Connection delegate;
    private final CA connectionAnalysis;

    public VendorConnection(Connection delegate) {
        this.delegate = delegate;
        try {
            this.connectionAnalysis = ((Class<CA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("Problem getting connectionAnalysis instance " + e.getMessage());
        }
    }

    public Connection getDelegate() {
        return this.delegate;
    }

    public void
}
