package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class ReaderConnectionArtifact extends Reader implements ConnectionArtifact_I<Reader> {

    protected final InnerState<Reader> innerState;
    public ReaderConnectionArtifact(Reader delegate,ConnectionContainer connectionContainer) {
        this.innerState = new InnerState<>(delegate, connectionContainer);
    }

    @Override
    public InnerState<Reader> getInnerState() {
        return innerState;
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        try {
            return getDelegate().read(target);
        }
        catch (IOException ioe) {
            throw (IOException) getConnectionContainer().getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public int read() throws IOException {
        try {
            return getDelegate().read();
        }
        catch (IOException ioe) {
            throw (IOException) getConnectionContainer().getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public int read(char[] cbuf) throws IOException {
        try {
            return getDelegate().read(cbuf);
        }
        catch (IOException ioe) {
            throw (IOException) getConnectionContainer().getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public boolean markSupported() {
        return getDelegate().markSupported();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        try {
            return getDelegate().read(cbuf, off, len);
        }
        catch (IOException ioe) {
            throw (IOException) getConnectionContainer().getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public long skip(long n) throws IOException {
        try {
            return getDelegate().skip(n);
        }
        catch (IOException ioe) {
            throw (IOException) getConnectionContainer().getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public boolean ready() throws IOException {
        try {
            return getDelegate().ready();
        }
        catch (IOException ioe) {
            throw (IOException) getConnectionContainer().getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        try {
            getDelegate().mark(readAheadLimit);
        }
        catch (IOException ioe) {
            throw (IOException) getConnectionContainer().getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public void reset() throws IOException {
        try {
            getDelegate().reset();
        }
        catch (IOException ioe) {
            throw (IOException) getConnectionContainer().getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.getDelegate().close();
        }
        catch (IOException ioe) {
            throw (IOException) getConnectionContainer().getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }
}
