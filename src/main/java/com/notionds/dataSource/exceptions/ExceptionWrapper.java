package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.ConnectionAction;

public class ExceptionWrapper extends Exception implements NotionExceptionWrapper {

    private final ConnectionAction connectionAction;

    public ExceptionWrapper(ConnectionAction connectionAction, Exception cause) {
        super(connectionAction.getDescription(), cause, false, false);
        this.connectionAction = connectionAction;
    }

    /**
     * This prevents a stack trace from being registered both for speed and because this wrapper
     * @return
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public ConnectionAction getRecommendation() {
        return this.connectionAction;
    }


}
