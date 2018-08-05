package com.notionds.dataSource.connection.accounting;

import java.time.Instant;

public class ExecuteAccounting {

    private final String sql;
    private final Instant startTime;
    private Instant finishTime;

    public ExecuteAccounting(String sql) {
        this.startTime = Instant.now();
        this.sql = sql;
    }

    public void setFinishTime() {
        if (finishTime == null) {
            this.finishTime = Instant.now();
        }
    }
    public Instant getStartTime() {
        return this.startTime;
    }
    public Instant getFinishTime() {
        return this.finishTime;
    }
    public String getSql() {
        return this.sql;
    }
}
