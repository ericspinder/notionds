package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.delegation.proxyV1.log.withLog.logging.DbObjectLogging;

import java.io.IOException;

public class IoExceptionWrapper extends IOException implements NotionExceptionWrapper {

    private final DbObjectLogging dbObjectLogging;

    public IoExceptionWrapper(DbObjectLogging dbObjectLogging, IOException cause) {
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
