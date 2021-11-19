package com.notionds.dataSupplier.operational;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public enum DurationOption implements Operational.Option<Duration> {

    ConnectionTimeoutInPool("com.notionds.connections_timeout_in_pool", "Amount of time connections will wait in the pool before reaping excess of the number of active in pool connections", Duration.of(20, ChronoUnit.MINUTES)),
    ConnectionTimeoutInPool_Cool_Down("com.notionds.connections_timeout_in_pool_cool_down", "Minimum amount of time between reaping extra active connections, this creates a walk down from the maximum number of connections", Duration.of(60, ChronoUnit.SECONDS)),
    ConnectionTimeoutOnLoan("com.notionds.connection_timeout_on_loan", "Default max time before connection is automatically closed, breaking loaned connections. Anything but a positive amount disables that function", Duration.of(3, ChronoUnit.MINUTES)),
    ConnectionMaxLifetime("com.notionds.connection_timeout_max_lifetime", "Max lifetime of a connection", Duration.of(2, ChronoUnit.HOURS)),
    RetrieveNewMember("com.notionds.retrieve_new", "The amount of time to wait for a new Member", Duration.of(3, ChronoUnit.SECONDS)),
    ;
    private final String i18n;
    private final String description;
    private final Duration defaultValue;

    DurationOption(String i18n, String description, Duration defaultValue) {
        this.i18n = i18n;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public String getI18n() {
        return this.i18n;
    }

    public String getDescription() {
        return this.description;
    }

    public Duration getDefaultValue() {
        return this.defaultValue;
    }
}
