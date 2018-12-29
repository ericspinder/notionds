package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.logging.CallableStatementLogging;
import com.notionds.dataSource.connection.logging.DbObjectLogging;
import com.notionds.dataSource.connection.logging.PreparedStatementLogging;
import com.notionds.dataSource.connection.logging.StatementLogging;
import com.notionds.dataSource.connection.delegation.DelegationOfNotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyDelegation<O extends Options> extends DelegationOfNotion<O> {

    private Logger logger = LoggerFactory.getLogger(ProxyDelegation.class);

    public ProxyDelegation(O options) {
        super(options);
    }

    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Object delegate, DbObjectLogging dbObjectLogging) {
        logger.trace("getDelegate(...Object....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        if (interfaces != null) {
            ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                    ProxyDelegation.class.getClassLoader(),
                    interfaces,
                    new ProxyMember(connectionContainer, delegate, dbObjectLogging));
            return connectionMember;
        }
        logger.error("No Interfaces");
        return null;

    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Statement delegate, StatementLogging statementLogging) {
        logger.trace("getDelegate(...Statement....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegation.class.getClassLoader(),
                interfaces,
                new StatementMember(connectionContainer, delegate, statementLogging));
        return connectionMember;
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, PreparedStatement delegate, Class clazz, PreparedStatementLogging preparedStatementLogging) {
        logger.trace("getDelegate(...PreparedStatement....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegation.class.getClassLoader(),
                interfaces,
                new PreparedMember(connectionContainer, delegate, preparedStatementLogging));
        return connectionMember;
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, CallableStatement delegate, Class clazz, CallableStatementLogging callableStatementAccounting) {
        logger.trace("getDelegate(...CallableStatement....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegation.class.getClassLoader(),
                interfaces,
                new CallableMember(connectionContainer, delegate, callableStatementAccounting));
        return connectionMember;
    }

    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, InputStream delegate, DbObjectLogging dbObjectLogging) {
        logger.trace("getDelegate(...InputStream....");
        return new InputStreamDelegate(connectionContainer, delegate, dbObjectLogging);
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Reader delegate, DbObjectLogging dbObjectLogging) {
        logger.trace("getDelegate(...Reader....");
        return new ReaderDelegate(connectionContainer, delegate, dbObjectLogging);
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, OutputStream delegate, DbObjectLogging dbObjectLogging) {
        logger.trace("getDelegate(...OutputStream....");
        return new OutputStreamDelegate(connectionContainer, delegate, dbObjectLogging);
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
