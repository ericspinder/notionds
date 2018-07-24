package com.notionds.dataSource.connection.accounting;

import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.exceptions.ExceptionAdvice;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class OperationAccounting {

    private final UUID connectionId;
    private final Instant startTime = Instant.now();
    private State state;
    private ExceptionAdvice.Recommendation recommendation;
    private Duration duration;

    public OperationAccounting(final UUID connectionId) {
        this.connectionId = connectionId;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Connection_Id=").append(connectionId);
        stringBuilder.append("Recommendation=").append(recommendation);
        stringBuilder.append("Duration=").append(duration.toString());
        return stringBuilder.toString();
    }
    public final UUID getConnectionId() {
        return connectionId;
    }

    public final ExceptionAdvice.Recommendation getRecommendation() {
        return recommendation;
    }

    /**
     * Update the recomendation with the most recent advice
     *
     * @param recommendation
     */
    public final OperationAccounting setRecommendation(final ExceptionAdvice.Recommendation recommendation) {
        this.recommendation = recommendation;
        return this;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public final OperationAccounting setFinishTime(Instant finishTime) {
        this.duration = Duration.between(this.startTime, finishTime);
        return this;
    }
    public final OperationAccounting finishNow() {
        return this.setFinishTime(Instant.now());
    }
    public final Duration getDuration() {
        return this.duration;
    }
    public final State getState() {
        return this.state;
    }
    public final OperationAccounting setState(State state) {
        this.state = state;
        return this;
    }
}
