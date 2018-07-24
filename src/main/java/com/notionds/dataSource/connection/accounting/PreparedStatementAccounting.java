package com.notionds.dataSource.connection.accounting;

import java.util.UUID;

public class PreparedStatementAccounting extends OperationAccounting {

    private final String sql;

    public PreparedStatementAccounting(UUID connectionId, String sql) {
        super(connectionId);
        this.sql = sql;
    }
}
