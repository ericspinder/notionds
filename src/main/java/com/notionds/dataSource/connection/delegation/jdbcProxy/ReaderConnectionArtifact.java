package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.time.Instant;
import java.util.UUID;

public class ReaderConnectionArtifact extends Reader implements ConnectionArtifact_I<Reader> {

    private final UUID uuid = UUID.randomUUID();
    private final Instant createInstant = Instant.now();
    private ConnectionContainer connectionContainer;
    private final Reader delegate;

    public ReaderConnectionArtifact(ConnectionContainer connectionContainer, Reader delegate) {
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
        if (this.connectionContainer == null) this.connectionContainer = connectionContainer;
    }

    @Override
    public Reader getDelegate() {
        return null;
    }

    @Override
    public Instant getCreateInstant() {
        return this.createInstant;
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        try {
            return delegate.read(target);
        }
        catch (IOException ioe) {
            throw (IOException) connectionContainer.getConnectionPool().throwBackProcessedException(ioe, this);
        }
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
    public int read(char[] cbuf) throws IOException {
        try {
            return delegate.read(cbuf);
        }
        catch (IOException ioe) {
            throw (IOException) connectionContainer.getConnectionPool().throwBackProcessedException(ioe, this);
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
            throw (IOException) connectionContainer.getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public long skip(long n) throws IOException {
        try {
            return delegate.skip(n);
        }
        catch (IOException ioe) {
            throw (IOException) connectionContainer.getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public boolean ready() throws IOException {
        try {
            return delegate.ready();
        }
        catch (IOException ioe) {
            throw (IOException) connectionContainer.getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        try {
            delegate.mark(readAheadLimit);
        }
        catch (IOException ioe) {
            throw (IOException) connectionContainer.getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public void reset() throws IOException {
        try {
            delegate.reset();
        }
        catch (IOException ioe) {
            throw (IOException) connectionContainer.getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    public void closeDelegate() {
        try {
            this.delegate.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.delegate.close();
        }
        catch (IOException ioe) {
            throw (IOException) connectionContainer.getConnectionPool().throwBackProcessedException(ioe, this);
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
        if (this.getArtifactId()== null) {
            if (other.getArtifactId() != null) {
                return false;
            }
        } else if (!this.getArtifactId().equals(other.getArtifactId())) {
            return false;
        }
        return true;
    }
}
