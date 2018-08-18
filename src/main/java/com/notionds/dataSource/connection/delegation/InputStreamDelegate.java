package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.accounting.OperationAccounting;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamDelegate extends InputStream implements ConnectionMember_I {

    private final InputStream delegate;
    private final ConnectionContainer connectionContainer;
    private final OperationAccounting operationAccounting;


    public InputStreamDelegate(ConnectionContainer connectionContainer, InputStream delegate, OperationAccounting operationAccounting) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
        this.operationAccounting = operationAccounting;
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
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
