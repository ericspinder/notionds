package com.notionds.dataSupplier.cron;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Task {

    private final String name;
    private final boolean doAsCleanup;
    private final Supplier<Result<?,?,?>> execution;
    private final Consumer<Exception> handler;
    private boolean canceled = false;

    public Task(final String name, final boolean doAsCleanup, final Supplier<Result<?,?,?>> execution, final Consumer<Exception> handler) {
        this.name = name;
        this.doAsCleanup = doAsCleanup;
        this.execution = execution;
        this.handler = handler;
    }

    public abstract Instant startTime();

    public boolean isDoAsCleanup() {
        return doAsCleanup;
    }

    public Supplier<Result<?,?,?>> execution() {
        return execution;
    }

    public Consumer<Exception> handler() {
        return handler;
    }
    public String name() {
        return this.name;
    }
    public boolean isCanceled() { return this.canceled; }

    public static class Countdown extends Task {
        private final Duration fromStart;

        public Countdown(final String name, final boolean registerForCleanup, final Supplier<Result<?,?,?>> execution, final Consumer<Exception> handler, final Duration fromStart) {
            super(name, registerForCleanup, execution, handler);
            this.fromStart = fromStart;
        }

        public Duration timeFromNow() {
            return fromStart;
        }

        @Override
        public Instant startTime() {
            return Instant.now().plus(fromStart);
        }
    }
    public static class SpecificTime extends Task {
        private final Instant instant;

        public SpecificTime(final String name, final boolean registerForCleanup, final Supplier<Result<?,?,?>> execution, final Consumer<Exception> handler, final Instant instant) {
            super(name,registerForCleanup,execution,handler);
            this.instant = instant;
        }

        public Instant instant() {
            return this.instant;
        }

        @Override
        public Instant startTime() {
            return this.instant;
        }
    }
    public static class CleanupOnly extends Task {

        public CleanupOnly(final String name, final boolean registerForCleanup, final Supplier<Result<?,?,?>> execution, final Consumer<Exception> handler) {
            super(name, registerForCleanup,execution,handler);
        }

        @Override
        public Instant startTime() {
            return null;
        }
    }
}
