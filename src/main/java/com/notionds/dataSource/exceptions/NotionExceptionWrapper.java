package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.delegation.proxyV1.log.withLog.logging.DbObjectLogging;

public interface NotionExceptionWrapper {

    DbObjectLogging getDbObjectLogging();
    Throwable getCause();
}
