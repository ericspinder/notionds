package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.logging.DbObjectLogging;

public class ExceptionWrapper extends Exception implements NotionExceptionWrapper {

    private final DbObjectLogging dbObjectLogging;

    public ExceptionWrapper(DbObjectLogging dbObjectLogging, Exception cause) {
        super(dbObjectLogging.toString(), cause, false, false);
        this.dbObjectLogging = dbObjectLogging;
    }

    public DbObjectLogging getDbObjectLogging() {
        return this.dbObjectLogging;
    }

}
