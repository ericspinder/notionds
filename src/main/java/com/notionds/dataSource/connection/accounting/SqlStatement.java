package com.notionds.dataSource.connection.accounting;

public interface SqlStatement {

    String getCurrentSql();

    ExecuteTimer startExecuteAccounting();
}
