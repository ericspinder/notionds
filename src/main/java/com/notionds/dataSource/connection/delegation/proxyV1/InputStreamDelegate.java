package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamDelegate extends InputStream implements ConnectionMember_I {

    protected final InputStream delegate;
    protected final ConnectionContainer connectionContainer;

    public InputStreamDelegate(ConnectionContainer connectionContainer, InputStream delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    @Override
    public int read() throws IOException {
        try {
            return delegate.read();
        }
        catch (IOException ioe) {
            throw connectionContainer.handleIoException(ioe, this);
        }
    }

    public void closeDelegate() {
        connectionContainer.getConnectionCleanup().close(this);
    }

    @Override
    public void close() throws IOException {
        this.closeDelegate();
    }

}
