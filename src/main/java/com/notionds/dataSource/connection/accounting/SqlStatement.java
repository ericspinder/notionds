package com.notionds.dataSource.connection.accounting;

public interface SqlStatement {

    String getCurrentSql();

    ExecuteAccounting startExecuteAccounting();
}
