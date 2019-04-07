package com.notionds.dataSource.connection.delegation.proxyV1.withLog;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
import com.notionds.dataSource.connection.delegation.proxyV1.OutputStreamDelegate;
import com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging.DbObjectLogging;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamDelegateWithLogging extends OutputStreamDelegate {

    private final DbObjectLogging dbObjectLogging;


    public OutputStreamDelegateWithLogging(ConnectionContainer connectionContainer, OutputStream delegate, DbObjectLogging dbObjectLogging) {
        super(connectionContainer, delegate);
        this.dbObjectLogging = dbObjectLogging;
    }

    public DbObjectLogging getDbObjectLogging() {
        return this.dbObjectLogging;
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
        super.flush();

    }
}
