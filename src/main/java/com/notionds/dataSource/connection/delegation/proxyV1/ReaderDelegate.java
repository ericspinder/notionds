package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.accounting.OperationAccounting;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class ReaderDelegate extends Reader implements ConnectionMember_I {

    private final ConnectionContainer connectionContainer;
    private final Reader delegate;
    private final OperationAccounting operationAccounting;

    public ReaderDelegate(ConnectionContainer connectionContainer, Reader delegate, OperationAccounting operationAccounting) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
        this.operationAccounting = operationAccounting;
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        try {
            return delegate.read(target);
        }
        catch (IOException ioe) {
            throw connectionContainer.handleIoException(ioe, this);
        }
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

    @Override
    public int read(char[] cbuf) throws IOException {
        try {
            return delegate.read(cbuf);
        }
        catch (IOException ioe) {
            throw connectionContainer.handleIoException(ioe, this);
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
            throw connectionContainer.handleIoException(ioe, this);
        }
    }

    @Override
    public long skip(long n) throws IOException {
        try {
            return delegate.skip(n);
        }
        catch (IOException ioe) {
            throw connectionContainer.handleIoException(ioe, this);
        }
    }

    @Override
    public boolean ready() throws IOException {
        try {
            return delegate.ready();
        }
        catch (IOException ioe) {
            throw connectionContainer.handleIoException(ioe, this);
        }
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        try {
            delegate.mark(readAheadLimit);
        }
        catch (IOException ioe) {
            throw connectionContainer.handleIoException(ioe, this);
        }
    }

    @Override
    public void reset() throws IOException {
        try {
            delegate.reset();
        }
        catch (IOException ioe) {
            throw connectionContainer.handleIoException(ioe, this);
        }
    }

    public void closeDelegate() throws IOException {
        this.connectionContainer.closeIoException(this);
    }

    @Override
    public void close() throws IOException {
        this.closeDelegate();
    }
}