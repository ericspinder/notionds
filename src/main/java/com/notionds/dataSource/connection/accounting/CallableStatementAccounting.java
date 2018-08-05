package com.notionds.dataSource.connection.accounting;

import java.util.UUID;

public class CallableStatementAccounting extends PreparedStatementAccounting implements SqlStatement {

    public CallableStatementAccounting(UUID connectionId, String sql) {
        super(connectionId, sql);
    }
}
