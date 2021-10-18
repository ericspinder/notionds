package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public final class InvokeAccounting {
    private final UUID connectionId;
    private final Instant startTime;
    private Duration duration;

    public InvokeAccounting(UUID connectionId) {
        this.startTime = Instant.now();
        this.connectionId = connectionId;
    }

    public void setFinishTime(Instant finishTime) {
        this.duration = Duration.between(this.startTime, finishTime);
    }
    public Instant getStartTime() {
        return this.startTime;
    }
    public Duration getDuration() {
        return this.duration;
    }
    public UUID getConnectionId() {
        return this.connectionId;
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("connectionId=").append(this.connectionId).append(", startTime=").append(this.startTime).append(", duration=").append(this.duration);
        return stringBuilder.toString();
    }
}
