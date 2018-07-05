package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.generator.ConnectionContainer;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class ReaderDelegate extends Reader implements ConnectionMember_I {

    private final ConnectionContainer connectionContainer;
    private final Reader delegate;
    private final OperationAccounting operationAccounting;

    public ReaderDelegate(ConnectionContainer connectionContainer, Reader delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
        this.operationAccounting = new OperationAccounting(connectionContainer.getConnectionId());
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        return delegate.read(target);
    }

    @Override
    public int read() throws IOException {
        return delegate.read();
    }

    @Override
    public int read(char[] cbuf) throws IOException {
        return delegate.read(cbuf);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return delegate.read(cbuf, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return delegate.skip(n);
    }

    @Override
    public boolean ready() throws IOException {
        return delegate.ready();
    }

    @Override
    public boolean markSupported() {
        return delegate.markSupported();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        delegate.mark(readAheadLimit);
    }

    @Override
    public void reset() throws IOException {
        delegate.reset();
    }

    @Override
    public void close() throws IOException {
        connectionContainer.closeIoException(this);
    }
}
