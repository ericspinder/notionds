package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.Recommendation;

import java.sql.SQLException;

public class SqlExceptionWrapper extends SQLException implements NotionExceptionWrapper {

    private final Recommendation recommendation;

    public SqlExceptionWrapper(Recommendation recommendation, SQLException cause) {
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
