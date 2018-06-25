package com.notionds.dataSource.connection.delegate;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.time.Instant;

public class ReaderDelegate<DM extends DelegateMapper> extends Reader {

    private final DM delegateMapper;
    private final Reader delegate;
    private final Instant createInstant = Instant.now();

    public ReaderDelegate(DM delegateMapper, Reader delegate) {
        this.delegateMapper = delegateMapper;
        this.delegate = delegate;
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
        delegateMapper.close(this);
        delegate.close();
    }
}
