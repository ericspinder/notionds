package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.logging.DbObjectLogging;

import java.sql.SQLException;

public class SqlExceptionWrapper extends SQLException implements NotionExceptionWrapper {


    private final DbObjectLogging dbObjectLogging;

    public SqlExceptionWrapper(DbObjectLogging dbObjectLogging, SQLException cause) {
        super(dbObjectLogging.toString(), cause);
        this.dbObjectLogging = dbObjectLogging;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public DbObjectLogging getDbObjectLogging() {
        return this.dbObjectLogging;
    }
}
