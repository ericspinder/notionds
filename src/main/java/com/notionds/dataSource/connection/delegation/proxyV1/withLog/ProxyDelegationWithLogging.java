package com.notionds.dataSource.connection.delegation.proxyV1.withLog;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
import com.notionds.dataSource.connection.delegation.proxyV1.ProxyDelegation;
import com.notionds.dataSource.connection.delegation.proxyV1.ProxyMember;
import com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging.CallableStatementLogging;
import com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging.DbObjectLogging;
import com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging.PreparedStatementLogging;
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

public class ProxyDelegationWithLogging<O extends Options> extends ProxyDelegation<O> {

    private Logger logger = LoggerFactory.getLogger(ProxyDelegationWithLogging.class);

    public ProxyDelegationWithLogging(O options) {
        super(options);
    }

    @Override
    protected ProxyMember createProxyMember(ConnectionContainer connectionContainer, Object delegate) {
        return new ProxyMember(connectionContainer, delegate);
    }
    @Override
    protected ProxyMember createProxyMember(ConnectionContainer connectionContainer, Statement delegate) {
        return new StatementMemberWithLogging(connectionContainer, delegate);
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
        return new InputStreamDelegateWithLogging(connectionContainer, delegate, dbObjectLogging);
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
