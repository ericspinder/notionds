package com.notionds.dataSource.connection;

import com.notionds.dataSource.OperationAccounting;

public interface ConnectionMember_I {

    ConnectionContainer getConnectionContainer();
    OperationAccounting getOperationAccounting();
    void closeDelegate() throws Exception;

}
