package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.logging.DbObjectLogging;

public interface ConnectionMember_I {

    ConnectionContainer getConnectionContainer();
    void closeDelegate() throws Exception;

}
