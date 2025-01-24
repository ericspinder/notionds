package com.notionds.dataSource.exceptions;

/**
 * https://en.wikipedia.org/wiki/SQLSTATE
 */
public enum Recommendation {

    Close_Closable("Close any Closeable associated with this connection", true, false),
    Burn_Pool_Failover("Error indicates a need to remove old connections to prevent more exceptions, then failover to next DB config", true, true),
    Authentication_Failover("Error indicates an authentication fail over is needed, current connections should be unaffected", false, true),
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

    @Override
    public String toString() {
        return "Recommendation{" + "description='" + description + '\'' +
                ", shouldCloseExisting=" + shouldCloseExisting +
                ", failoverToNextConnectionSupplier=" + failoverToNextConnectionSupplier +
                '}';
    }
}
