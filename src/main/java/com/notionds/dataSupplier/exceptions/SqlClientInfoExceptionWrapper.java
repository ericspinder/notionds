package com.notionds.dataSupplier.exceptions;

import java.sql.SQLClientInfoException;

public class SqlClientInfoExceptionWrapper extends SQLClientInfoException implements NotionExceptionWrapper {

    private final Recommendation recommendation;

    public SqlClientInfoExceptionWrapper(String message, Recommendation recommendation, SQLClientInfoException cause) {
        super(message, cause.getFailedProperties(), cause);
        this.recommendation = recommendation;
    }
    /**
     * Use of 'this' as the return prevents a stack trace from being registered.
     * This is a wrapper and has nothing to do with the creation of the exception.
     * @return this
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public Recommendation getRecommendation() {
        return this.recommendation;
    }


}
