package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.generator.ConnectionContainer;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class NotionConnection_Keep_for_now<NW extends ConnectionContainer> implements Connection, ConnectionMember_I {

    protected final NW notionWrapper;
    private final OperationAccounting operationAccounting;
    private State state = State.Open;

    public NotionConnection_Keep_for_now(NW notionWrapper) {
        this.notionWrapper = notionWrapper;
        this.operationAccounting = new OperationAccounting(notionWrapper.getConnectionId());
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    @Override
    public final void close() throws SQLException {
        this.notionWrapper.close(this);
    }

    @Override
    protected final void closeDelegate () {

    }

    @Override
    public final boolean isClosed() throws SQLException {
        return State.Closed.equals(this.getState());
    }


}
