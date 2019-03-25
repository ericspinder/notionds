package com.notionds.dataSource.connection.delegation.proxyV1.withLog;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
import com.notionds.dataSource.connection.delegation.proxyV1.InputStreamDelegate;
import com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging.DbObjectLogging;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamDelegateWithLogging extends InputStreamDelegate {

    private final DbObjectLogging dbObjectLogging;


    public InputStreamDelegateWithLogging(ConnectionContainer connectionContainer, InputStream delegate, DbObjectLogging dbObjectLogging) {
        super(connectionContainer, delegate);
        this.dbObjectLogging = dbObjectLogging;
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

    public void closeDelegate() {
        connectionContainer.getConnectionCleanup().close(this);
    }

    @Override
    public void close() throws IOException {
        this.closeDelegate();
    }

}
