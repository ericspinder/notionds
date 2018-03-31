package com.notionds.dataSource.connection;

import com.notionds.dataSource.Options;

import java.sql.Connection;
import java.time.Instant;

public abstract class NotionConnection<O extends Options> implements Connection {

    protected final O options;
    protected final Instant startTime;
    protected final Connection delegate;

    public NotionConnection(O options, Connection delegate) {
        this.options = options;
        this.delegate = delegate;
        this.startTime = Instant.now();
    }
    public final Instant getStartTime() {
        return this.startTime;
    }
}
