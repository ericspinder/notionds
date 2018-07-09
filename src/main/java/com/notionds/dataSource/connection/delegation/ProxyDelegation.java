package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;

import java.lang.reflect.Proxy;

public class ProxyDelegation<O extends Options> extends DelegationOfNotion<O> {

    public ProxyDelegation(O options) {
        super(options);
    }

    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Object delegate, Class clazz) {

        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegation.class.getClassLoader(),
                new java.lang.Class[] {clazz, ConnectionMember_I.class},
                new ProxyMember(connectionContainer, delegate));
        return connectionMember;
    }
}
