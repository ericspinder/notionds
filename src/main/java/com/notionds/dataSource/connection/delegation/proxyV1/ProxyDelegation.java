package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
import com.notionds.dataSource.connection.delegation.DelegationOfNotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyDelegation<O extends Options> extends DelegationOfNotion<O> {

    private Logger logger = LoggerFactory.getLogger(ProxyDelegation.class);

    public ProxyDelegation(O options) {
        super(options);
    }

    @Override
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Object delegate, Class clazz, String maybeSql) {
        logger.trace("getDelegate(...Object....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        if (interfaces != null) {
            ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                    ProxyDelegation.class.getClassLoader(),
                    interfaces,
                    createProxyMember(connectionContainer, delegate));
            return connectionMember;
        }
        logger.error("No Interfaces");
        return null;

    }

    protected ProxyMember createProxyMember(ConnectionContainer connectionContainer, Object delegate) {
        return new ProxyMember(connectionContainer, delegate);
    }

    private Class[] getConnectionMemberInterfaces(Class clazz)  {
        if (interfacesCache.containsKey(clazz.getCanonicalName())) {
            return interfacesCache.get(clazz.getCanonicalName());
        }
        Class[] oldArray = clazz.getInterfaces();
        if (oldArray.length == 0) {
            return null;
        }
        Class[] newArray = new Class[oldArray.length + 1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        newArray[oldArray.length] = ConnectionMember_I.class;
        interfacesCache.put(clazz.getCanonicalName(), newArray);
        return newArray;
    }
    private Map<String, Class[]> interfacesCache = new ConcurrentHashMap<>();
}
