package com.notionds.dataSource.connection;

import com.notionds.dataSource.connection.accounting.OperationAccounting;

public interface ConnectionMember_I {

    ConnectionContainer getConnectionContainer();
    OperationAccounting getOperationAccounting();
    void closeDelegate() throws Exception;

}
