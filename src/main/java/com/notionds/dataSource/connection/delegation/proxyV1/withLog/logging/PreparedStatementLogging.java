package com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging;

import com.notionds.dataSource.Options;

import java.util.UUID;

public abstract class PreparedStatementLogging<O extends Options> extends DbObjectLogging<O> {

    private final String sql;
    private ExecuteTimer executeTimer;

    public PreparedStatementLogging(O options, UUID connectionId, String sql) {
        super(options, connectionId);
        this.sql = sql;
    }

    public void executeStart() {

    }

    public void executeEnd() {

    }

    public String getCurrentSql() {
        return this.sql;
    }
}
