package com.notionds.dataSource.connection.delegate;

import com.notionds.dataSource.connection.ConnectionMember_I;

public abstract class ConnectionMember<DM extends DelegateMapper, AW extends AutoCloseable> implements ConnectionMember_I<DM, AW> {

    protected final AW delegate;
    protected final DM delegateMapper;


    public ConnectionMember(DM delegateMapper, AW delegate) {
        this.delegateMapper = delegateMapper;
        this.delegate = delegate;
    }
}
