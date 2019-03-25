package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging.DbObjectLogging;

import java.sql.SQLClientInfoException;

public class SqlClientInfoExceptionWrapper extends SQLClientInfoException implements NotionExceptionWrapper {

    private final DbObjectLogging dbObjectLogging;

    public SqlClientInfoExceptionWrapper(DbObjectLogging dbObjectLogging, SQLClientInfoException cause) {
        super(dbObjectLogging.toString(), cause.getFailedProperties(), cause);
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
