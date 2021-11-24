package com.notionds.dataSupplier.delegation.reflection;

import com.notionds.dataSupplier.Container;
import com.notionds.dataSupplier.delegation.Wrapper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class OutputStreamWrapper extends OutputStream implements Wrapper<OutputStream> {

    private UUID uuid = UUID.randomUUID();
    protected final OutputStream delegate;
    protected final Container<OutputStream,?,?> container;

    public OutputStreamWrapper(Container<OutputStream,?,?> container, OutputStream delegate) {
        this.container = container;
        this.delegate = delegate;
    }
    @Override
    public UUID getArtifactId() {
        return this.uuid;
    }
    @Override
    public Container<OutputStream,?,?> getContainer() {
        return this.container;
    }

    @Override
    public OutputStream getDelegate() {
        return this.delegate;
    }

    @Override
    public void close() throws IOException {
        this.flush();
        this.container.closeDelegate();
    }

    @Override
    public void write(int b) throws IOException {
        try {
            delegate.write(b);
        }
        catch(IOException ioe) {
            container.handleException(ioe, this);
            throw ioe;
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            delegate.flush();
        }
        catch(IOException ioe) {
            container.handleException(ioe, this);
            throw ioe;
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
        if (!(that instanceof Wrapper)) {
            return false;
        }
        if (this.getArtifactId() == null) {
            if (((Wrapper)that).getArtifactId() != null) {
                return false;
            }
        } else if (!this.getArtifactId().equals(((Wrapper)that).getArtifactId())) {
            return false;
        }
        return true;
    }

}
