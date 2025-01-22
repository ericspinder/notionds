package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

public class InputStreamConnectionArtifact extends InputStream implements ConnectionArtifact_I<InputStream> {

    private final UUID uuid = UUID.randomUUID();
    private final Instant createInstant = Instant.now();
    protected final InputStream delegate;
    protected final ConnectionContainer connectionContainer;

    public InputStreamConnectionArtifact(ConnectionContainer connectionContainer, InputStream delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
    }

    @Override
    public UUID getArtifactId() {
        return this.uuid;
    }
    @Override
    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    @Override
    public void setConnectionContainer(ConnectionContainer connectionContainer) {

    }

    @Override
    public InputStream getDelegate() {
        return delegate;
    }

    @Override
    public Instant getCreateInstant() {
        return createInstant;
    }

    @Override
    public int read() throws IOException {
        try {
            return delegate.read();
        }
        catch (IOException ioe) {
            throw (IOException) connectionContainer.getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public void close() throws IOException {
        this.delegate.close();
    }
    @Override
    public final boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (!(that instanceof ConnectionArtifact_I<?> other)) {
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
