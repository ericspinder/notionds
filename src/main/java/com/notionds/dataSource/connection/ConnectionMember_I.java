package com.notionds.dataSource.connection;

import com.notionds.dataSource.connection.logging.DbObjectLogging;

public interface ConnectionMember_I {

    ConnectionContainer getConnectionContainer();
    DbObjectLogging getDbObjectLogging();
    void closeDelegate() throws Exception;

}
