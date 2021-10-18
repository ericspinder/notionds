package com.notionds.dataSource.exceptions;

/**
 * https://en.wikipedia.org/wiki/SQLSTATE
 */
public enum Recommendation {

    Close_Closable("Close any Closeable associated with this connection", true),
    Database_Failover("Error that shows a database failover is needed", true),
    Burn_Pool_Failover("Error indicates a need to remove old connections to prevent more exceptions, then failover to next DB config", true),
    Authentication_Failover("Error indicates an authentication fail over is needed", true),
    Version_Fail("Error indicative of a coding problem", true),
    Nominal_Operation("Nominal Operation", false),
    ;
    private final String description;
    private final boolean shouldClose;

    Recommendation(String description, boolean shouldClose) {
        this.description = description;
        this.shouldClose = shouldClose;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean shouldClose() {
        return this.shouldClose;
    }
}
