package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.manual9.ConnectionContainerManual9;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamDelegate<NW extends ConnectionContainerManual9> extends InputStream implements ConnectionMember_I {

    protected final InputStream delegate;
    protected final NW notionWrapper;
    private final OperationAccounting operationAccounting;
    private State state = State.Open;


    public InputStreamDelegate(NW notionWrapper, InputStream delegate) {
        this.notionWrapper = notionWrapper;
        this.delegate = delegate;
        this.operationAccounting = new OperationAccounting(notionWrapper.getConnectionId());
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public int read() throws IOException {
        return delegate.read();
    }

    protected void closeDelegate() throws IOException {
        this.delegate.close();
    }
    @Override
    public void close() throws IOException {
        this.notionWrapper.close(this);
    }

}
