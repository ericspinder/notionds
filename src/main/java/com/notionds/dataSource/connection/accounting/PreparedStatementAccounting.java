package com.notionds.dataSource.connection.accounting;

import java.util.UUID;

public abstract class PreparedStatementAccounting extends OperationAccounting implements SqlStatement {

    private final String sql;

    public PreparedStatementAccounting(UUID connectionId, String sql) {
        super(connectionId);
        this.sql = sql;
    }

    public String getCurrentSql() {
        return this.sql;
    }
}
