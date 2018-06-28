package com.notionds.dataSource.connection;

import java.sql.*;
import java.time.Instant;

public abstract class NotionConnection<NW extends NotionWrapper> implements Connection, ConnectionMember_I {
    
    protected final Instant createInstant = Instant.now();
    protected final NW notionWrapper;

    public NotionConnection(NW notionWrapper) {
        this.notionWrapper = notionWrapper;
    }
    public final Instant getCreateInstant() {
        return this.createInstant;
    }
    @Override
    public final void close() throws SQLException {
        this.notionWrapper.closeNotionConnectionTree();
    }

    @Override
    public final boolean isClosed() throws SQLException {
        try {
            return notionWrapper.getUnderlyingVendorConnection().isClosed();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }


}
