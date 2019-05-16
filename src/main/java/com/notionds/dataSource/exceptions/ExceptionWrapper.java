package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.delegation.proxyV1.log.withLog.DbObjectLogging;

public class ExceptionWrapper extends Exception implements NotionExceptionWrapper {

    private final ExceptionAdvice.Recommendation recommendation;

    public ExceptionWrapper(ExceptionAdvice.Recommendation recommendation, Exception cause) {
        super(recommendation.getDescription(), cause, false, false);
        this.recommendation = recommendation;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public ExceptionAdvice.Recommendation getRecommendation() {
        return this.recommendation;
    }


}
