package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.Recommendation;

import java.sql.SQLClientInfoException;

public class SqlClientInfoExceptionWrapper extends SQLClientInfoException implements NotionExceptionWrapper {

    private final Recommendation recommendation;

    public SqlClientInfoExceptionWrapper(Recommendation recommendation, SQLClientInfoException cause) {
        super(recommendation.getDescription(), cause.getFailedProperties(), cause);
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
