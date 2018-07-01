package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public abstract class NoCloseNeededNotion<NW extends ConnectionContainer> implements DatabaseMetaData, ConnectionMember_I {

    protected final NW notionWrapper;
    protected final DatabaseMetaData delegate;
    private final OperationAccounting operationAccounting;
    private State state = State.Open;

    public NoCloseNeededNotion(NW notionWrapper, DatabaseMetaData delegate) {
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

    @Override
    public void close() throws SQLException {
        this.notionWrapper.close(this);
    }

    @Override
    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    protected void closeDelegate() throws SQLException {
        // no operation needed
    }
}
