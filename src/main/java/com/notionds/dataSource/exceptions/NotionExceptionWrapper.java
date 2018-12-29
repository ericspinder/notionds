package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.logging.DbObjectLogging;

public interface NotionExceptionWrapper {

    DbObjectLogging getDbObjectLogging();
    Throwable getCause();
}
