package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.connection.Container;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class InputStreamConnectionArtifact extends InputStream implements ConnectionArtifact_I {

    private final UUID uuid = UUID.randomUUID();
    protected final InputStream delegate;
    protected final Container<?,?,?> container;

    public InputStreamConnectionArtifact(Container<?,?,?> container, InputStream delegate) {
        this.container = container;
        this.delegate = delegate;
    }

    @Override
    public UUID getArtifactId() {
        return this.uuid;
    }
    @Override
    public Container<?,?,?> getContainer() {
        return this.container;
    }

    @Override
    public Object getDelegate() {
        return this.delegate;
    }

    @Override
    public int read() throws IOException {
        try {
            return delegate.read();
        }
        catch (IOException ioe) {
            throw container.handleIoException(ioe, this);
        }
    }

    @Override
    public void close() throws IOException {
        this.container.closeDelegate(this);
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
