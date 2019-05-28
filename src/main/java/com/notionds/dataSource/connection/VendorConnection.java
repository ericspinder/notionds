package com.notionds.dataSource.connection;

import com.notionds.dataSource.DatabaseMain;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.Recommendation;

import java.sql.Connection;

public abstract class VendorConnection<O extends Options> {

    private final O options;
    private final DatabaseMain databaseMain;
    private final Connection delegate;


    @SuppressWarnings("unchecked")
    public VendorConnection(O options, DatabaseMain databaseMain, Connection delegate) {
        this.options = options;
        this.databaseMain = databaseMain;
        this.delegate = delegate;


    }
    protected DatabaseMain getDatabaseMain() {
        return this.databaseMain;
    }

    public Connection getDelegate() {
        return this.delegate;
    }

    public void release(Recommendation recommendation) {
        this.databaseMain.release(this, recommendation);

    }



}
