package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import java.time.Duration;
import java.time.Instant;

public final class InvokeAccounting {
    private final String methodName;
    private final String sql;
    private final Instant startTime;
    private Duration duration;

    public InvokeAccounting(String methodName, String sql) {
        this.methodName = methodName;
        this.sql = sql;
        this.startTime = Instant.now();
    }

    public String getSql() {
        return this.sql;
    }
    public void setFinishTime(Instant finishTime) {
        this.duration = Duration.between(this.startTime, finishTime);
    }
    public Duration getDuration() {
        return this.duration;
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("method = ").append(methodName).append(", sql = ").append(sql);
        if (duration != null) {
            stringBuilder.append(", seconds = ").append(duration.getSeconds()).append('.').append(duration.getNano());
        }
        else {
            stringBuilder.append(", no duration available - startTime = ").append(startTime.toString());
        }
        return stringBuilder.toString();
    }
}
