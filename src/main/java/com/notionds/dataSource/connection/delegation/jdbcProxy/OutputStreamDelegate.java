package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.connection.ConnectionMain;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamDelegate extends OutputStream implements ConnectionMember_I {

    protected final OutputStream delegate;
    protected final ConnectionMain connectionMain;
    protected boolean isClosed = false;


    public OutputStreamDelegate(ConnectionMain connectionMain, OutputStream delegate) {
        this.connectionMain = connectionMain;
        this.delegate = delegate;
    }

    public ConnectionMain getConnectionMain() {
        return this.connectionMain;
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
    public void write(int b) throws IOException {
        try {
            delegate.write(b);
        }
        catch(IOException ioe) {
            throw connectionMain.handleIoException(ioe, this);
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            delegate.flush();
        }
        catch(IOException ioe) {
            throw connectionMain.handleIoException(ioe, this);
        }
    }

    @Override
    public boolean isClosed() {
        return this.isClosed;
    }
}
