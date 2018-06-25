package com.notionds.dataSource.connection;

import com.notionds.dataSource.Options;

import java.sql.Connection;
import java.time.Instant;
import java.util.UUID;

public abstract class NotionConnection<O extends Options> implements Connection, ConnectionMember_I {

    protected final O options;
    protected final Instant createInstant = Instant.now();
    protected final Connection delegate;

    public NotionConnection(O options, Connection delegate) {
        this.options = options;
        this.delegate = delegate;
    }
    public final Instant getCreateInstant() {
        return this.createInstant;
    }

}
