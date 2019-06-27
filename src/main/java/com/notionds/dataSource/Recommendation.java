package com.notionds.dataSource;

/**
 * https://en.wikipedia.org/wiki/SQLSTATE
 */
public enum Recommendation {

    CloseConnectionInstance("Close Connection", true),
    FailoverDatabase_Now("Failover Database, Now!", true),
    FailoverDatabase_When_Finished("Failover Database, when finished", true),
    NoAction("No additional action", false),
    ReturnToPool("Return to Pool (if it exists)", false)
    ;
    private final String description;
    private final boolean closeVendorConnection;

    Recommendation(String description, boolean closeVendorConnection) {
        this.description = description;
        this.closeVendorConnection = closeVendorConnection;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isCloseVendorConnection() {
        return this.closeVendorConnection;
    }
}
