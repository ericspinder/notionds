package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamDelegate extends OutputStream implements ConnectionMember_I {

    protected final OutputStream delegate;
    protected final ConnectionContainer connectionContainer;


    public OutputStreamDelegate(ConnectionContainer connectionContainer, OutputStream delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    public void closeDelegate() {
        connectionContainer.getConnectionCleanup().close(this);
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
}
