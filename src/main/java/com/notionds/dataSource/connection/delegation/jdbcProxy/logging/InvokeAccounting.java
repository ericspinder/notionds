package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public final class InvokeAccounting {
    private final Instant startTime;
    private Duration duration;

    public InvokeAccounting() {
        this.startTime = Instant.now();
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
    public String toString() {
        return ", startTime=" + this.startTime + ", duration=" + this.duration;
    }
}
