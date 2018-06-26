package com.notionds.dataSource.connection;

import com.notionds.dataSource.ExceptionHandler;
import com.notionds.dataSource.Options;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;

public abstract class VendorConnection<O extends Options, CA extends ConnectionAnalysis> {

    private final O options;
    private final Connection delegate;
    private final CA connectionAnalysis;

    public VendorConnection(O options, Connection delegate) {
        this.options = options;
        this.delegate = delegate;
        try {
            this.connectionAnalysis = ((Class<CA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor().newInstance();
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
