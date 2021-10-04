package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamConnectionArtifact extends OutputStream implements ConnectionArtifact_I {

    protected final OutputStream delegate;
    protected final ConnectionContainer connectionContainer;

    public OutputStreamConnectionArtifact(ConnectionContainer connectionContainer, OutputStream delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
    }

    public ConnectionContainer getConnectionMain() {
        return this.connectionContainer;
    }

    public void closeDelegate() {
        connectionContainer.getConnectionCleanup().cleanup(this, this.delegate);
    }
    @Override
    public void close() throws IOException {
        this.closeDelegate();
    }

    @Override
    public void write(int b) throws IOException {
        try {
            delegate.write(b);
        }
        catch(IOException ioe) {
            throw connectionContainer.handleIoException(ioe, this);
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            delegate.flush();
        }
        catch(IOException ioe) {
            throw connectionContainer.handleIoException(ioe, this);
        }
    }

}
