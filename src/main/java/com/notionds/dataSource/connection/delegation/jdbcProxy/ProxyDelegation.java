package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionMain;
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
    public final ConnectionMember_I getDelegate(ConnectionMain connectionMain, Object delegate, Class delegateClassReturned, Object[] args) {
        logger.trace("getDelegate(...Object....");
        if (delegateClassReturned.isInterface()) {
            Class[] interfaces = this.getConnectionMemberInterfaces(delegateClassReturned);
            if (interfaces != null) {
                return this.getProxyMember(interfaces, connectionMain, delegate, args);
            }
            logger.error("No Interfaces, very odd as it's an interface: " + delegateClassReturned.getCanonicalName());
            throw new RuntimeException("No Interfaces, very odd as it's an interface: " + delegateClassReturned.getCanonicalName());
        }
        else if ( delegate instanceof InputStream) {
            return this.createInputStreamDelegate(connectionMain, (InputStream) delegate, args);
        }
        else if (delegate instanceof OutputStream) {
            return this.createOutputStreamDelegate(connectionMain, (OutputStream) delegate, args);
        }
        else if (delegate instanceof Reader) {
            return this.createReaderDelegate(connectionMain, (Reader) delegate, args);
        }
        logger.error("ProxyDelegation is unable to create: " + delegateClassReturned.getCanonicalName());
        throw new RuntimeException("ProxyDelegation is unable to create: " + delegateClassReturned.getCanonicalName());
    }

    protected ConnectionMember_I getProxyMember(Class[] interfaces, ConnectionMain connectionMain, Object delegate, Object[] args) {
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegation.class.getClassLoader(),
                interfaces,
                createProxyMember(connectionMain, delegate, args));
        return connectionMember;
    }

    protected ConnectionMember_I createInputStreamDelegate(ConnectionMain connectionMain, InputStream delegate, Object[] args) {
        return new InputStreamDelegate(connectionMain, delegate);
    }

    protected ConnectionMember_I createOutputStreamDelegate(ConnectionMain connectionMain, OutputStream delegate, Object[] args) {
        return new OutputStreamDelegate(connectionMain, delegate);
    }

    protected ConnectionMember_I createReaderDelegate(ConnectionMain connectionMain, Reader delegate, Object[] args) {
        return new ReaderDelegate(connectionMain, delegate);
    }

    protected ProxyMember createProxyMember(ConnectionMain connectionMain, Object delegate, Object[] args) {
        return new ProxyMember(connectionMain, delegate);
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
