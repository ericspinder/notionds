package com.notionds.dataSource.connection;

import java.time.Duration;
import java.time.Instant;

public class Timer {

    private final Instant startTime;
    private Instant endTime;

    public Timer() {
        this.startTime = Instant.now();
    }

    public Timer(Instant startTime) {
        this.startTime = startTime;
    }
    public Duration getTotalTime(Instant endTime) {
        this.endTime = endTime;
        return Duration.between(startTime, endTime);
    }
    public Duration getDuration() {
        if (this.endTime != null) {
            return Duration.between(startTime, endTime);
        }
        else {
            return Duration.between(startTime, Instant.now());
        }
    }
}
