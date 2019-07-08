package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.ConnectionMain;

public interface ConnectionMember_I {

    ConnectionMain getConnectionMain();

    void closeDelegate() throws Exception;

    boolean isClosed();

}
