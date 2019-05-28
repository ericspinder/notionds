package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.Recommendation;

public class ExceptionWrapper extends Exception implements NotionExceptionWrapper {

    private final Recommendation recommendation;

    public ExceptionWrapper(Recommendation recommendation, Exception cause) {
        super(recommendation.getDescription(), cause, false, false);
        this.recommendation = recommendation;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public Recommendation getRecommendation() {
        return this.recommendation;
    }


}
