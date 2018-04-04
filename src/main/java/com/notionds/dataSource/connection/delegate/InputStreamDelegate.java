package com.notionds.dataSource.connection.delegate;

import com.notionds.dataSource.connection.ConnectionMember_I;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamDelegate<DM extends DelegateMapper> extends InputStream implements ConnectionMember_I<DM, InputStream> {

    protected final InputStream delegate;
    protected final DM delegateMapper;


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
}
