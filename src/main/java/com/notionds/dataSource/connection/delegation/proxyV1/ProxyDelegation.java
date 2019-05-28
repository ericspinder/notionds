package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
import com.notionds.dataSource.connection.delegation.DelegationOfNotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyDelegation<O extends Options> extends DelegationOfNotion<O> {

    private Logger logger = LoggerFactory.getLogger(ProxyDelegation.class);

    public ProxyDelegation(O options) {
        super(options);
    }

    @Override
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Object delegate, Class delegateClassReturned, Object[] args) {
        logger.trace("getDelegate(...Object....");
        if (delegateClassReturned.isInterface()) {
            Class[] interfaces = this.getConnectionMemberInterfaces(delegateClassReturned);
            if (interfaces != null) {
                return this.getProxyMember(interfaces, connectionContainer, delegate);
            }
            logger.error("No Interfaces, very odd as it's an interface: " + delegateClassReturned.getCanonicalName());
            throw new RuntimeException("No Interfaces, very odd as it's an interface: " + delegateClassReturned.getCanonicalName());
        }
        else if ( delegate instanceof InputStream) {
            return new InputStreamDelegate(connectionContainer, (InputStream) delegate);
        }
        else if (delegate instanceof OutputStream) {
            return new OutputStreamDelegate(connectionContainer, (OutputStream) delegate);
        }
        else if (delegate instanceof Reader) {
            return new ReaderDelegate(connectionContainer, (Reader) delegate);
        }
        logger.error("ProxyDelegation is unable to create: " + delegateClassReturned.getCanonicalName());
        throw new RuntimeException("ProxyDelegation is unable to create: " + delegateClassReturned.getCanonicalName());
    }

    protected ConnectionMember_I getProxyMember(Class[] interfaces, ConnectionContainer connectionContainer, Object delegate) {
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegation.class.getClassLoader(),
                interfaces,
                createProxyMember(connectionContainer, delegate));
        return connectionMember;
    }

    protected ProxyMember createProxyMember(ConnectionContainer connectionContainer, Object delegate) {
        return new ProxyMember(connectionContainer, delegate);
    }

    protected Class[] getConnectionMemberInterfaces(Class clazz)  {
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
    protected Map<String, Class[]> interfacesCache = new ConcurrentHashMap<>();
}
