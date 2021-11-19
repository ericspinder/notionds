package com.notionds.dataSupplier.exceptions;

import java.io.IOException;

public class IoExceptionWrapper extends IOException implements NotionExceptionWrapper {

    private final Recommendation recommendation;

    public IoExceptionWrapper(String message, Recommendation recommendation, IOException cause) {
        super(message, cause);
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
