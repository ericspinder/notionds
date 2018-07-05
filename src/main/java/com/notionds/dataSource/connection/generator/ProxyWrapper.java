package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionMember_I;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyWrapper<O extends Options> extends WrapperOfNotion<O> {

    public ProxyWrapper(O options) {
        super(options);
    }

    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Object delegate, Class clazz) {

        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyWrapper.class.getClassLoader(),
                new java.lang.Class[] {clazz, ConnectionMember_I.class},
                new ProxyMember(connectionContainer, delegate));
        return connectionMember;
    }
}
