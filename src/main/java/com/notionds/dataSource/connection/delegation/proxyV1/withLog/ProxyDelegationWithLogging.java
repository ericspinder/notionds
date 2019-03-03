package com.notionds.dataSource.connection.delegation.proxyV1.withLog;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
import com.notionds.dataSource.connection.delegation.DelegationOfNotion;
import com.notionds.dataSource.connection.logging.CallableStatementLogging;
import com.notionds.dataSource.connection.logging.DbObjectLogging;
import com.notionds.dataSource.connection.logging.PreparedStatementLogging;
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

public class ProxyDelegationWithLogging<O extends Options> extends DelegationOfNotion<O> {

    private Logger logger = LoggerFactory.getLogger(ProxyDelegationWithLogging.class);

    public ProxyDelegationWithLogging(O options) {
        super(options);
    }

    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Object delegate) {
        logger.trace("getDelegate(...Object....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        if (interfaces != null) {
            ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                    ProxyDelegationWithLogging.class.getClassLoader(),
                    interfaces,
                    new ProxyMemberWithLogging(connectionContainer, delegate, dbObjectLogging));
            return connectionMember;
        }
        logger.error("No Interfaces");
        return null;

    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Statement delegate) {
        logger.trace("getDelegate(...Statement....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegationWithLogging.class.getClassLoader(),
                interfaces,
                new StatementMemberWithLogging(connectionContainer, delegate));
        return connectionMember;
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, PreparedStatement delegate, Class clazz, PreparedStatementLogging preparedStatementLogging) {
        logger.trace("getDelegate(...PreparedStatement....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegationWithLogging.class.getClassLoader(),
                interfaces,
                new PreparedStatementMemberWithLogging(connectionContainer, delegate, preparedStatementLogging));
        return connectionMember;
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, CallableStatement delegate, Class clazz, CallableStatementLogging callableStatementAccounting) {
        logger.trace("getDelegate(...CallableStatement....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegationWithLogging.class.getClassLoader(),
                interfaces,
                new CallableMemberWithLogging(connectionContainer, delegate, callableStatementAccounting));
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
    private Map<String, Class[]> interfacesCache = new ConcurrentHashMap<>();
}
