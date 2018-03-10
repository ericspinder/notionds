package com.notionds.dataSource.connection;

public abstract class DelegatedInstance<DM extends DelegateMapper, AW extends AutoCloseable> {

    protected final AW delegate;
    protected final DM delegateMapper;


    public DelegatedInstance(DM delegateMapper, AW delegate) {
        this.delegateMapper = delegateMapper;
        this.delegate = delegate;
    }
}
