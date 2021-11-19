package com.notionds.dataSupplier.delegation.refelction;

import com.notionds.dataSupplier.Container;
import com.notionds.dataSupplier.delegation.Wrapper;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.UUID;

public class ReaderMember extends Reader implements Wrapper<Reader> {

    private final UUID uuid = UUID.randomUUID();
    private final Container<Reader,?,?> container;
    private final Reader delegate;

    public ReaderMember(Container<Reader,?,?> container, Reader delegate) {
        this.container = container;
        this.delegate = delegate;
    }
    @Override
    public UUID getArtifactId() {
        return this.uuid;
    }
    @Override
    public Container<Reader,?,?> getContainer() {
        return this.container;
    }

    @Override
    public Reader getDelegate() {
        return this.delegate;
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        try {
            return delegate.read(target);
        }
        catch (IOException ioe) {
            container.handleException(ioe, this);
            throw ioe;
        }
    }

    @Override
    public int read() throws IOException {
        try {
            return delegate.read();
        }
        catch (IOException ioe) {
            container.handleException(ioe, this);
            throw ioe;
        }
    }

    @Override
    public int read(char[] cbuf) throws IOException {
        try {
            return delegate.read(cbuf);
        }
        catch (IOException ioe) {
            container.handleException(ioe, this);
            throw ioe;
        }
    }

    @Override
    public boolean markSupported() {
        return delegate.markSupported();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        try {
            return delegate.read(cbuf, off, len);
        }
        catch (IOException ioe) {
            container.handleException(ioe, this);
            throw ioe;
        }
    }

    @Override
    public long skip(long n) throws IOException {
        try {
            return delegate.skip(n);
        }
        catch (IOException ioe) {
            container.handleException(ioe, this);
            throw ioe;
        }
    }

    @Override
    public boolean ready() throws IOException {
        try {
            return delegate.ready();
        }
        catch (IOException ioe) {
            container.handleException(ioe, this);
            throw ioe;
        }
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        try {
            delegate.mark(readAheadLimit);
        }
        catch (IOException ioe) {
            container.handleException(ioe, this);
            throw ioe;
        }
    }

    @Override
    public void reset() throws IOException {
        try {
            delegate.reset();
        }
        catch (IOException ioe) {
            container.handleException(ioe, this);
            throw ioe;
        }
    }

    @Override
    public void close() throws IOException {
        this.container.closeDelegate();
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
