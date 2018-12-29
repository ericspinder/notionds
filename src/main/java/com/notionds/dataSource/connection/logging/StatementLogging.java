package com.notionds.dataSource.connection.logging;

import com.notionds.dataSource.Options;

import java.util.UUID;

public abstract class StatementLogging<O extends Options> extends DbObjectLogging<O> {

    private String currentSql;

    public StatementLogging(O options, UUID connectionId) {
        super(options, connectionId);
    }

    public void setSql(String sql) {
        this.currentSql = sql;
    }

    public String getCurrentSql() {
        return this.currentSql;
    }

    public void executeStart(String sql) {
        this.currentSql = sql;
        if (options.get())
    }
}
