package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.ConnectionAction;

import java.sql.SQLClientInfoException;

public class SqlClientInfoExceptionWrapper extends SQLClientInfoException implements NotionExceptionWrapper {

    private final ConnectionAction connectionAction;

    public SqlClientInfoExceptionWrapper(ConnectionAction connectionAction, SQLClientInfoException cause) {
        super(connectionAction.getDescription(), cause.getFailedProperties(), cause);
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
