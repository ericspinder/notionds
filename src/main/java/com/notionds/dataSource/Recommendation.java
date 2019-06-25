package com.notionds.dataSource;

/**
 * https://en.wikipedia.org/wiki/SQLSTATE
 */
public enum Recommendation {

    CloseConnectionInstance_Now("Close Connection, Now!"),
    CloseConnectionInstance_When_Finished("Close Connection, when finished"),
    FailoverDatabase_Now("Failover Database, Now!"),
    FailoverDatabase_When_Finished("Failover Database, when finished"),
    NoAction("No additional action")
    ;
    private String description;
    Recommendation(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }
}