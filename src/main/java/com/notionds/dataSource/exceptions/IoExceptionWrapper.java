package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.Recommendation;

import java.io.IOException;

public class IoExceptionWrapper extends IOException implements NotionExceptionWrapper {

    private final Recommendation recommendation;

    public IoExceptionWrapper(Recommendation recommendation, IOException cause) {
        super(recommendation.getDescription(), cause);
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
