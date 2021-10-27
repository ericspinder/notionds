package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.connection.Container;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class OutputStreamConnectionArtifact extends OutputStream implements ConnectionArtifact_I {

    private UUID uuid = UUID.randomUUID();
    protected final OutputStream delegate;
    protected final Container container;

    public OutputStreamConnectionArtifact(Container container, OutputStream delegate) {
        this.container = container;
        this.delegate = delegate;
    }
    @Override
    public UUID getArtifactId() {
        return this.uuid;
    }
    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public Object getDelegate() {
        return this.delegate;
    }

    @Override
    public void close() throws IOException {
        this.flush();
        this.container.closeDelegate(this);
    }

    @Override
    public void write(int b) throws IOException {
        try {
            delegate.write(b);
        }
        catch(IOException ioe) {
            throw container.handleIoException(ioe, this);
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            delegate.flush();
        }
        catch(IOException ioe) {
            throw container.handleIoException(ioe, this);
        }
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
