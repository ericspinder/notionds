package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.ConnectionAction;

import java.sql.SQLException;

public class SqlExceptionWrapper extends SQLException implements NotionExceptionWrapper {

    private final ConnectionAction connectionAction;

    public SqlExceptionWrapper(ConnectionAction connectionAction, SQLException cause) {
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
