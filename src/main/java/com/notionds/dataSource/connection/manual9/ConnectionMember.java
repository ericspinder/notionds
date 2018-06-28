package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;

import java.time.Instant;
import java.util.UUID;

public abstract class ConnectionMember<DM extends NotionWrapperManual9, AW extends AutoCloseable> implements ConnectionMember_I {

    protected final AW delegate;
    protected final DM notionWrapper;
    protected final Instant createInstant = Instant.now();
    private State state = State.Open;


    public ConnectionMember(DM NotionWrapper, AW delegate) {
        this.notionWrapper = NotionWrapper;
        this.delegate = delegate;
    }
    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public final Instant getCreateInstant() {
        return this.createInstant;
    }

    @Override
    public UUID getConnectionId() {
        return this.notionWrapper.getConnectionId();
    }
}

