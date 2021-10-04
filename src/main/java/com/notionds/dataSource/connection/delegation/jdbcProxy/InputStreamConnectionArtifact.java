package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamConnectionArtifact extends InputStream implements ConnectionArtifact_I {

    protected final InputStream delegate;
    protected final ConnectionContainer connectionContainer;

    public InputStreamConnectionArtifact(ConnectionContainer connectionContainer, InputStream delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
    }

    public ConnectionContainer getConnectionMain() {
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
        connectionContainer.getConnectionCleanup().cleanup(this, this.delegate);
    }

    @Override
    public void close() throws IOException {
        this.closeDelegate();
    }
}
