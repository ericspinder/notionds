package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;

import java.sql.SQLException;

public abstract class ConnectionMember<NW extends ConnectionContainer, AW extends AutoCloseable> implements ConnectionMember_I {

    protected final AW delegate;
    protected final NW notionWrapper;
    protected final OperationAccounting operationAccounting;
    private State state = State.Open;


    public ConnectionMember(NW NotionWrapper, AW delegate) {
        this.notionWrapper = NotionWrapper;
        this.delegate = delegate;
        this.operationAccounting = new OperationAccounting(notionWrapper.getConnectionId());
    }
    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public final OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    protected void closeDelegate() {

    }

    @Override
    public final void close() throws SQLException {

    }

}

