package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.generator.ConnectionContainer;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamDelegate extends InputStream implements ConnectionMember_I {

    private final InputStream delegate;
    private final ConnectionContainer connectionContainer;
    private final OperationAccounting operationAccounting;


    public InputStreamDelegate(ConnectionContainer connectionContainer, InputStream delegate) {
        this.connectionContainer = connectionContainer;
        this.delegate = delegate;
        this.operationAccounting = new OperationAccounting(connectionContainer.getConnectionId());
    }

    public ConnectionContainer getConnectionContainer() {
        return this.connectionContainer;
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    @Override
    public int read() throws IOException {
        return delegate.read();
    }

    public void closeDelegate() throws IOException {
        this.connectionContainer.closeIoException(this);
    }
    @Override
    public void close() throws IOException {
        this.closeDelegate();
    }

}
