package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.delegation.proxyV1.log.withLog.DbObjectLogging;

import java.sql.SQLClientInfoException;

public class SqlClientInfoExceptionWrapper extends SQLClientInfoException implements NotionExceptionWrapper {

    private final ExceptionAdvice.Recommendation recommendation;

    public SqlClientInfoExceptionWrapper(ExceptionAdvice.Recommendation recommendation, SQLClientInfoException cause) {
        super(recommendation.getDescription(), cause.getFailedProperties(), cause);
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
