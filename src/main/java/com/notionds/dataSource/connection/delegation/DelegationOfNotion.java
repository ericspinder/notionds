package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.accounting.OperationAccounting;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;

public abstract class DelegationOfNotion<O extends Options> {

    protected final O options;

    public DelegationOfNotion(O options) {
        this.options = options;
    }


    public abstract ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Object delegate, Class clazz, OperationAccounting operationAccounting);

}
