package com.notionds.dataSource.exceptions;

/**
 * https://en.wikipedia.org/wiki/SQLSTATE
 */
public enum Recommendation {

    Close_Closable("Close any Closeable associated with this connection", false),
    Burn_Pool_Failover("Error indicates a need to remove old connections to prevent more exceptions, then failover to next DB config", true),
    Authentication_Failover("Error indicates an authentication fail over is needed, current connections should be unaffected", true),
    Nominal_Operation("Nominal Operation", false),
    ;
    private final String description;
    private final boolean failoverToNextConnectionSupplier;

    Recommendation(String description, boolean failoverToNextConnectionSupplier) {
        this.description = description;
        this.failoverToNextConnectionSupplier = failoverToNextConnectionSupplier;
    }

    public String getDescription() {
        return this.description;
    }


    public boolean isFailoverToNextConnectionSupplier() {
        return this.failoverToNextConnectionSupplier;
    }

    @Override
    public String toString() {
        return "Recommendation{" + "description='" + description + '\'' +
                ", failoverToNextConnectionSupplier=" + failoverToNextConnectionSupplier +
                '}';
    }
}
