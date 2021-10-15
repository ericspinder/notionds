package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.connection.Cleanup;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class InputStreamConnectionArtifact extends InputStream implements ConnectionArtifact_I {

    private final UUID uuid = UUID.randomUUID();
    protected final InputStream delegate;
    protected final ConnectionContainer<?,?,?,?> connectionContainer;

    public InputStreamConnectionArtifact(ConnectionContainer<?,?,?,?> connectionContainer, InputStream delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
    }

    @Override
    public UUID getArtifactId() {
        return this.uuid;
    }
    @Override
    public ConnectionContainer<?,?,?,?> getConnectionMain() {
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
        Cleanup.DoDelegateClose(this.delegate);
    }

    @Override
    public void close() throws IOException {
        this.connectionContainer.closeChild(this);
    }
    @Override
    public final boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (!(that instanceof ConnectionArtifact_I other)) {
            return false;
        }
        if (this.getArtifactId() == null) {
            if (other.getArtifactId() != null) {
                return false;
            }
        } else if (!this.getArtifactId().equals(other.getArtifactId())) {
            return false;
        }
        return true;
    }
}
