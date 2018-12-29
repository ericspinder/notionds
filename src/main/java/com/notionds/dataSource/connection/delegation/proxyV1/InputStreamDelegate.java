package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.logging.DbObjectLogging;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamDelegate extends InputStream implements ConnectionMember_I {

    private final InputStream delegate;
    private final ConnectionContainer connectionContainer;
    private final DbObjectLogging dbObjectLogging;


    public InputStreamDelegate(ConnectionContainer connectionContainer, InputStream delegate, DbObjectLogging dbObjectLogging) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
        this.dbObjectLogging = dbObjectLogging;
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    public DbObjectLogging getDbObjectLogging() {
        return this.dbObjectLogging;
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

    public void closeDelegate() throws IOException {
        this.connectionContainer.closeIoException(this);
    }

    @Override
    public void close() throws IOException {
        this.closeDelegate();
    }

}
