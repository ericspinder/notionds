package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;

import java.sql.Clob;
import java.sql.SQLException;

public abstract class ClobOfNotion<NW extends ConnectionContainer> implements Clob, ConnectionMember_I {

    protected final NW notionWrapper;
    protected final Clob delegate;
    private final OperationAccounting operationAccounting;
    private State state = State.Open;

    public ClobOfNotion(NW notionWrapper, Clob delegate) {
        this.notionWrapper = notionWrapper;
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

    @Override
    public void close() throws SQLException {
        this.notionWrapper.close(this);
    }
}
