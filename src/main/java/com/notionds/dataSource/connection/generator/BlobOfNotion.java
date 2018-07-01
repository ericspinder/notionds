package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;

import java.sql.Blob;
import java.sql.SQLException;

public abstract class BlobOfNotion<NW extends ConnectionContainer> implements Blob, ConnectionMember_I {

    protected final NW notionWrapper;
    protected final Blob delegate;
    private final OperationAccounting operationAccounting;
    private State state = State.Open;

    public BlobOfNotion(NW delegatedMapper, Blob delegate) {
        this.notionWrapper = delegatedMapper;
        this.delegate = delegate;
        this.operationAccounting = new OperationAccounting(delegatedMapper.getConnectionId());
    }

    public final State getState() {
        return this.state;
    }

    public final void setState(State state) {
        this.state = state;
    }

    public final OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

    @Override
    public final void close() throws SQLException {
        this.notionWrapper.close(this, delegate);
    }

}
