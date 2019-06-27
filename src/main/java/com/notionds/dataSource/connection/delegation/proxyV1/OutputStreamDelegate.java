package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamDelegate extends OutputStream implements ConnectionMember_I {

    protected final OutputStream delegate;
    protected final ConnectionContainer connectionContainer;
    protected boolean isClosed = false;


    public OutputStreamDelegate(ConnectionContainer connectionContainer, OutputStream delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    public void closeDelegate() {
        this.isClosed = true;
        connectionContainer.getConnectionCleanup().cleanup(this, this.delegate);
    }
    @Override
    public void close() throws IOException {
        this.closeDelegate();
    }

    @Override
    public void write(int b) throws IOException {
        try {
            delegate.write(b);
        }
        catch(IOException ioe) {
            throw connectionContainer.handleIoException(ioe, this);
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            delegate.flush();
        }
        catch(IOException ioe) {
            throw connectionContainer.handleIoException(ioe, this);
        }
    }

    @Override
    public boolean isClosed() {
        return this.isClosed;
    }
}
