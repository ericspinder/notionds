package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.manual9.ConnectionContainerManual9;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class ReaderDelegate<NW extends ConnectionContainerManual9> extends Reader implements ConnectionMember_I {

    private final NW notionWrapper;
    private final Reader delegate;
    private final OperationAccounting operationAccounting;
    private State state = State.Open;

    public ReaderDelegate(NW notionWrapper, Reader delegate) {
        this.notionWrapper = notionWrapper;
        this.delegate = delegate;
        this.operationAccounting = new OperationAccounting(notionWrapper.getConnectionId());
    }
    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }


    protected void closeDelegate() throws IOException {
        this.delegate.close();
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
        notionWrapper.close(this);
    }
}
