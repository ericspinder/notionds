package com.notionds.dataSource.connection.delegate;

import com.notionds.dataSource.connection.ConnectionMember_I;

public abstract class DelegateTree {

    public DelegateTree() {}

    public abstract void put(ConnectionMember_I parent, ConnectionMember_I child);

    public abstract DelegateTree get(ConnectionMember_I parent);

    public abstract DelegateTree remove(ConnectionMember_I parent);
}
