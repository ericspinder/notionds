package com.notionds.dataSupplier.exceptions;

import java.util.Collection;
import java.util.Collections;

/**
 * https://en.wikipedia.org/wiki/SQLSTATE
 */
public enum Recommendation {

    Close_Closable("Close any Closeable associated with this connection", true, false),
    Database_Failover("Error that shows a database failover is needed", true, true),
    Burn_Pool_Failover("Error indicates a need to remove old connections to prevent more exceptions, then failover to next DB config", true, true),
    Authentication_Failover("Error indicates an authentication fail over is needed", false, true),
    Version_Fail("Error indicative of a coding problem", true, false),
    Nominal_Operation("Nominal Operation", false, false),
    ;
    private final String description;
    private final boolean shouldCloseExisting;
    private final boolean failoverToNextConnectionSupplier;

    Recommendation(String description, boolean shouldCloseExisting, boolean failoverToNextConnectionSupplier) {
        this.description = description;
        this.shouldCloseExisting = shouldCloseExisting;
        this.failoverToNextConnectionSupplier = failoverToNextConnectionSupplier;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean shouldClose() {
        return this.shouldCloseExisting;
    }

    public boolean isFailoverToNextConnectionSupplier() {
        return this.failoverToNextConnectionSupplier;
    }

    public boolean isInCollection(Collection<Recommendation> collection) {
        return Collections.frequency(collection, this) > 0;
    }
}
