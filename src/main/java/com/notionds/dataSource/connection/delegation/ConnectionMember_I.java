package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.ConnectionContainer;

public interface ConnectionMember_I {

    ConnectionContainer getConnectionContainer();

    void closeDelegate() throws Exception;

    boolean isClosed();

}
