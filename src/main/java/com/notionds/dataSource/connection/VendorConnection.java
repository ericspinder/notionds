 package com.notionds.dataSource.connection;

import com.notionds.dataSource.DatabaseMain;
import com.notionds.dataSource.Options;

import java.lang.ref.ReferenceQueue;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;

public abstract class VendorConnection<O extends Options, CA extends ConnectionAnalysis> {

    private final O options;
    private final DatabaseMain databaseMain;
    private final Connection delegate;
    private final CA connectionAnalysis;
    private final ReferenceQueue<ConnectionMember_I> referenceQueue = new ReferenceQueue<>();

    public VendorConnection(O options, DatabaseMain databaseMain, Connection delegate) {
        this.options = options;
        this.databaseMain = databaseMain;
        this.delegate = delegate;
        try {
            this.connectionAnalysis = ((Class<CA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("Problem getting connectionAnalysis instance " + e.getMessage());
        }

    }
    public DatabaseMain getDatabaseMain() {
        return this.databaseMain;
    }

    public CA getConnectionAnalysis() {
        return this.connectionAnalysis;
    }

    public Connection getDelegate() {
        return this.delegate;
    }

    public ReferenceQueue<ConnectionMember_I> getReferenceQueue() {
        return this.referenceQueue;
    }

}
