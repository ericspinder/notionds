package com.notionds.dataSource.connection.cleanup;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.Recommendation;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
import com.notionds.dataSource.connection.VendorConnection;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.sql.Connection;

public abstract class ConnectionCleanup<O extends Options, NC extends NotionCleanup, VC extends VendorConnection> {

    protected final O options;
    protected final NC notionCleanup;
    protected final VC vendorConnection;
    protected boolean stable;

    public ConnectionCleanup(O options, NC notionCleanup, VC vendorConnection) {
        this.options = options;
        this.notionCleanup = notionCleanup;
        this.vendorConnection = vendorConnection;
        this.stable = true;
    }

    public abstract Connection getConnection(ConnectionContainer connectionContainer);

    public abstract void cleanup(boolean closeConnectionDelegate);

    public abstract void cleanup();

    public abstract void cleanup(ConnectionMember_I connectionMember);

    public abstract ConnectionMember_I add(ConnectionMember_I connectionMember, Object delegate, ConnectionMember_I parent);

    public void reviewException(ConnectionMember_I connectionMember, NotionExceptionWrapper exceptionWrapper) {
        this.vendorConnection.
        if (exceptionWrapper.getRecommendation().equals(Recommendation.CloseConnectionInstance_Now)) {
            this.vendorConnection.
        }
    }

}
