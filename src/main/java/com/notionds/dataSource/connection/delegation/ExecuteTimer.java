package com.notionds.dataSource.connection.delegation;

import java.time.Duration;
import java.time.Instant;

public class ExecuteTimer {

    private final String sql;
    private final Instant startTime;
    private Duration duration;

    public ExecuteTimer(String sql) {
        this.startTime = Instant.now();
        this.sql = sql;
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
    public String getSql() {
        return this.sql;
    }
    public String getProtectedSql() {
        if (this.sql != null && this.sql.length()> 0) {
            String firstWord = this.sql.substring(0, this.sql.indexOf(' '));
            if (firstWord.equalsIgnoreCase("SELECT")) {
                return this.sql.substring(0, this.sql)
            }
        }
    }
}
