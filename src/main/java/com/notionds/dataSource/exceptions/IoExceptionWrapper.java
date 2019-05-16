package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.delegation.proxyV1.log.withLog.DbObjectLogging;

import java.io.IOException;

public class IoExceptionWrapper extends IOException implements NotionExceptionWrapper {

    private final ExceptionAdvice.Recommendation recommendation;

    public IoExceptionWrapper(ExceptionAdvice.Recommendation recommendation, IOException cause) {
        super(recommendation.getDescription(), cause);
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
