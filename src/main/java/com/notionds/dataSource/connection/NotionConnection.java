package com.notionds.dataSource.connection;

import java.sql.*;
import java.time.Instant;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public abstract class NotionConnection<NW extends NotionWrapper> implements Connection, ConnectionMember_I {
    
    protected final Instant createInstant = Instant.now();
    protected final NW notionWrapper;

    public NotionConnection(NW notionWrapper) {
        this.notionWrapper = notionWrapper;
    }
    public final Instant getCreateInstant() {
        return this.createInstant;
    }


}
