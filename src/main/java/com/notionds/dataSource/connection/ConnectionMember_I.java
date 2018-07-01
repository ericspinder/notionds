package com.notionds.dataSource.connection;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.connection.generator.ConnectionContainer;

public interface ConnectionMember_I extends AutoCloseable {

    ConnectionContainer getConnectionContainer();
    OperationAccounting getOperationAccounting();
    State getState();
    void setState(State state);

}
