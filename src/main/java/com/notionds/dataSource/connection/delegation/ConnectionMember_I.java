package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.logging.DbObjectLogging;

public interface ConnectionMember_I<L extends DbObjectLogging> {

    ConnectionContainer getConnectionContainer();
    L getDbObjectLogging();
    void closeDelegate() throws Exception;

}
