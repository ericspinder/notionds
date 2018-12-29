package com.notionds.dataSource.connection.logging;

import com.notionds.dataSource.Options;

import java.util.UUID;

public abstract class PreparedStatementLogging<O extends Options> extends DbObjectLogging<O> {

    private final String sql;

    public PreparedStatementLogging(O options, UUID connectionId, String sql) {
        super(options, connectionId);
        this.sql = sql;
    }

    public abstract void executeStart();

    public abstract void executeEnd();

    public String getCurrentSql() {
        return this.sql;
    }
}
