package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.connection.ConnectionMember_I;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

public class InputStreamDelegate<DM extends NotionWrapperManual9> extends InputStream implements ConnectionMember_I {

    protected final InputStream delegate;
    protected final DM delegateMapper;
    private final Instant createInstant = Instant.now();


    public InputStreamDelegate(DM delegateMapper, InputStream delegate) {
        this.delegateMapper = delegateMapper;
        this.delegate = delegate;
    }

    @Override
    public int read() throws IOException {
        return delegate.read();
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
