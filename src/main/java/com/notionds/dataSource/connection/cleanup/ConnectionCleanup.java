package com.notionds.dataSource.connection.cleanup;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.VendorConnection;

import java.sql.Connection;

public abstract class ConnectionCleanup<O extends Options, NC extends NotionCleanup, VC extends VendorConnection> {

    protected final O options;
    protected final NC notionCleanup;
    protected final VC vendorConnection;

    public ConnectionCleanup(O options, NC notionCleanup, VC vendorConnection) {
        this.options = options;
        this.notionCleanup = notionCleanup;
        this.vendorConnection = vendorConnection;
    }

    public abstract Connection getConnection(ConnectionContainer connectionContainer);

    public abstract void closeAll();

    public abstract void close(ConnectionMember_I connectionMember);

    public abstract ConnectionMember_I add(ConnectionMember_I connectionMember, Object delegate, ConnectionMember_I parent);

}
