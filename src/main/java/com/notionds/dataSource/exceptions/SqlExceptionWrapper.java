package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.delegation.proxyV1.log.withLog.DbObjectLogging;

import java.sql.SQLException;

public class SqlExceptionWrapper extends SQLException implements NotionExceptionWrapper {

    private final ExceptionAdvice.Recommendation recommendation;

    public SqlExceptionWrapper(ExceptionAdvice.Recommendation recommendation, SQLException cause) {
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
