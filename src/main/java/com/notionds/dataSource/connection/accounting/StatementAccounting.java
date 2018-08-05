package com.notionds.dataSource.connection.accounting;

import java.util.UUID;

public abstract class StatementAccounting extends OperationAccounting implements SqlStatement {

    public StatementAccounting(UUID connectionId) {
        super(connectionId);
    }

    public abstract void setSql(String sql);
}
