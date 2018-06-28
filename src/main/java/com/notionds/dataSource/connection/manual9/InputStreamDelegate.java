package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

public class InputStreamDelegate<DM extends NotionWrapperManual9> extends InputStream implements ConnectionMember_I {

    protected final InputStream delegate;
    protected final DM delegateMapper;
    private final Instant createInstant = Instant.now();
    private State state = State.Open;


    public InputStreamDelegate(DM delegateMapper, InputStream delegate) {
        this.delegateMapper = delegateMapper;
        this.delegate = delegate;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }
    @Override
    public int read() throws IOException {
        return delegate.read();
    }

    @Override
    public void closeDelegate() throws IOException {
        this.delegate.close();
    }
    @Override
    public void close() throws IOException {
        this.delegateMapper.close(this);
    }

    public UUID getConnectionId() {
        return this.delegateMapper.getConnectionId();
    }

    public Instant getCreateInstant() {
        return this.createInstant;
    }
}
