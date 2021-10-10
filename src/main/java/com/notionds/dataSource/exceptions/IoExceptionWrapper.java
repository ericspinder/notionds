package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.ConnectionAction;

import java.io.IOException;

public class IoExceptionWrapper extends IOException implements NotionExceptionWrapper {

    private final ConnectionAction connectionAction;

    public IoExceptionWrapper(ConnectionAction connectionAction, IOException cause) {
        super(connectionAction.getDescription(), cause);
        this.connectionAction = connectionAction;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public ConnectionAction getRecommendation() {
        return this.connectionAction;
    }

}
