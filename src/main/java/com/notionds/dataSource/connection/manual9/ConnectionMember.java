package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.connection.ConnectionMember_I;

import java.time.Instant;
import java.util.UUID;

public abstract class ConnectionMember<DM extends NotionWrapperManual9, AW extends AutoCloseable> implements ConnectionMember_I {

    protected final AW delegate;
    protected final DM delegateMapper;
    protected final Instant createInstant = Instant.now();


    public ConnectionMember(DM delegateMapper, AW delegate) {
        this.delegateMapper = delegateMapper;
        this.delegate = delegate;
    }

    public final Instant getCreateInstant() {
        return this.createInstant;
    }

    @Override
    public UUID getConnectionId() {
        return this.delegateMapper.getConnectionId();
    }
}
