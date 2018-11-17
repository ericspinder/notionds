package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.accounting.OperationAccounting;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamDelegate extends OutputStream implements ConnectionMember_I {

    private final OutputStream delegate;
    private final ConnectionContainer connectionContainer;
    private final OperationAccounting operationAccounting;


    public OutputStreamDelegate(ConnectionContainer connectionContainer, OutputStream delegate, OperationAccounting operationAccounting) {
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


    public void closeDelegate() throws IOException {
        this.connectionContainer.closeIoException(this);
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
