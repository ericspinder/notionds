package com.notionds.dataSource.connection;

import com.notionds.dataSource.ExceptionAdvice;

import java.time.Instant;
import java.util.UUID;

public class ExceptionAccounting {

    private final UUID connectionId;
    private ExceptionAdvice.Recommendation recommendation;
    private final Instant exceptionTime;

    public ExceptionAccounting(final UUID connectionId, final ExceptionAdvice.Recommendation recommendation, final Instant exceptionTime) {
        this.connectionId = connectionId;
        this.recommendation = recommendation;
        this.exceptionTime = exceptionTime;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Connection Id = ").append(connectionId);
        stringBuilder.append("Recommendation = ").append(recommendation.getDescription());
        stringBuilder.append("Exception Time = ").append(exceptionTime);
        return stringBuilder.toString();
    }
    public UUID getConnectionId() {
        return connectionId;
    }

    public ExceptionAdvice.Recommendation getRecommendation() {
        return recommendation;
    }

    /**
     * Update the recomendation with the most recent advice
     *
     * @param recommendation
     */
    public void setRecommendation(final ExceptionAdvice.Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public Instant getExceptionTime() {
        return exceptionTime;
    }


}
