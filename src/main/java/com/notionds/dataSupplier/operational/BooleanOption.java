package com.notionds.dataSupplier.operational;

public enum BooleanOption implements Operational.Option<Boolean> {

    ConnectionContainer_Check_ResultSet("com.notion.connectionMain.checkResultSet", "Order a check of all ResultSets before closing when cleanupAfterGC() had not been called, until the connection had been closed", true),
    ConnectionPool_Use("com.notion.pool.usePool", "Should pool connections", true),
    Logging("com.notion.connection.delegation.jdbcProxy.logging.UseLogging", "Use ProxyV1 logging", false),
    LogNonExecuteProxyMembers("com.notion.connection.delegation.jdbcProxy.logging.LogNonExecuteProxyMembers", "Use a proxy wrapper for even non-execute proxy member classes, when logging is turned on", false),
    EqualsByUUID("com.notionds.wrapper.equalsByUUID", "Allow the wrapper Id to override the equals", false);
    private final String i18n;
    private final String description;
    private final Boolean defaultValue;

    BooleanOption(String i18n, String description, Boolean defaultValue) {
        this.i18n = i18n;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public String getI18n() {
        return i18n;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean getDefaultValue() {
        return this.defaultValue;
    }
}
