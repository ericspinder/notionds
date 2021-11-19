package com.notionds.dataSupplier.exceptions;

public class ExceptionWrapper extends Exception implements NotionExceptionWrapper {

    private final Recommendation recommendation;

    public ExceptionWrapper(String message, Recommendation recommendation, Exception cause) {
        super(message, cause, false, false);
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
