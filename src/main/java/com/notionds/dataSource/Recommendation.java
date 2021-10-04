package com.notionds.dataSource;

/**
 * https://en.wikipedia.org/wiki/SQLSTATE
 */
public enum Recommendation {

    CloseCloseable("Close Closeable", true),
    FailoverDatabase_Now("Failover Database, Now!", true),
    FailoverDatabase_When_Finished("Failover Database, when finished", true),
    NoAction("No additional action", false),
    ReturnToPool("Return to Pool (if it exists)", false)
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
