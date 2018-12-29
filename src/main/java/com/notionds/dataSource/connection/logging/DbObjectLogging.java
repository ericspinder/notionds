package com.notionds.dataSource.connection.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.exceptions.ExceptionAdvice;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public abstract class DbObjectLogging<O extends Options> {

    private final UUID connectionId;
    private final Instant startTime = Instant.now();
    private State state;
    private ExceptionAdvice.Recommendation recommendation;
    private Duration duration;
    protected final O options;

    public DbObjectLogging(O options, final UUID connectionId) {
        this.options = options;
        this.connectionId = connectionId;
    }

    public final UUID getConnectionId() {
        return connectionId;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public final DbObjectLogging setFinishTime(Instant finishTime) {
        this.duration = Duration.between(this.startTime, finishTime);
        return this;
    }
    public final DbObjectLogging finishNow() {
        return this.setFinishTime(Instant.now());
    }
    public final Duration getDuration() {
        return this.duration;
    }

    public abstract void startInvoke(Object proxy, Method m, Object[] args);
    public abstract void exception(NotionExceptionWrapper notionExceptionWrapper);
    public abstract void endInvoke();

}
