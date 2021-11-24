package com.notionds.dataSupplier.delegation.reflection.aggregation;

import java.time.Duration;
import java.time.Instant;

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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(", startTime=").append(this.startTime).append(", duration=").append(this.duration);
        return stringBuilder.toString();
    }
}
