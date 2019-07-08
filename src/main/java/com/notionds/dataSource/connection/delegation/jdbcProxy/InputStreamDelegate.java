package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.connection.ConnectionMain;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamDelegate extends InputStream implements ConnectionMember_I {

    protected final InputStream delegate;
    protected final ConnectionMain connectionMain;
    protected boolean isClosed = false;

    public InputStreamDelegate(ConnectionMain connectionMain, InputStream delegate) {
        this.connectionMain = connectionMain;
        this.delegate = delegate;
    }

    public ConnectionMain getConnectionMain() {
        return this.connectionMain;
    }

    @Override
    public int read() throws IOException {
        try {
            return delegate.read();
        }
        catch (IOException ioe) {
            throw connectionMain.handleIoException(ioe, this);
        }
    }

    public void closeDelegate() {
        this.isClosed = true;
        connectionMain.getConnectionCleanup().cleanup(this, this.delegate);
    }

    @Override
    public void close() throws IOException {
        this.closeDelegate();
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

}
