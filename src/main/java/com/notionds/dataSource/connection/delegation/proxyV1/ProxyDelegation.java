package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.accounting.CallableStatementAccounting;
import com.notionds.dataSource.connection.accounting.OperationAccounting;
import com.notionds.dataSource.connection.accounting.PreparedStatementAccounting;
import com.notionds.dataSource.connection.accounting.StatementAccounting;
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
import java.util.ArrayList;
import java.util.List;

public class ProxyDelegation<O extends Options> extends DelegationOfNotion<O> {

    private Logger logger = LoggerFactory.getLogger(ProxyDelegation.class);

    public ProxyDelegation(O options) {
        super(options);
    }

    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Object delegate, OperationAccounting operationAccounting) {
        logger.trace("getDelegate(...Object....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        if (interfaces != null) {
            ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                    ProxyDelegation.class.getClassLoader(),
                    interfaces,
                    new ProxyMember(connectionContainer, delegate, operationAccounting));
            return connectionMember;
        }
        logger.error("No Interfaces");
        return null;

    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Statement delegate, StatementAccounting statementAccounting) {
        logger.trace("getDelegate(...Statement....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegation.class.getClassLoader(),
                interfaces,
                new StatementMember(connectionContainer, delegate, statementAccounting));
        return connectionMember;
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, PreparedStatement delegate, Class clazz, PreparedStatementAccounting preparedStatementAccounting) {
        logger.trace("getDelegate(...PreparedStatement....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegation.class.getClassLoader(),
                interfaces,
                new PreparedMember(connectionContainer, delegate, preparedStatementAccounting));
        return connectionMember;
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, CallableStatement delegate, Class clazz, CallableStatementAccounting callableStatementAccounting) {
        logger.trace("getDelegate(...CallableStatement....");
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegation.class.getClassLoader(),
                interfaces,
                new CallableMember(connectionContainer, delegate, callableStatementAccounting));
        return connectionMember;
    }

    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, InputStream delegate, OperationAccounting operationAccounting) {
        logger.trace("getDelegate(...InputStream....");
        return new InputStreamDelegate(connectionContainer, delegate, operationAccounting);
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Reader delegate, OperationAccounting operationAccounting) {
        logger.trace("getDelegate(...Reader....");
        return new ReaderDelegate(connectionContainer, delegate, operationAccounting);
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, OutputStream delegate, OperationAccounting operationAccounting) {
        logger.trace("getDelegate(...OutputStream....");
        return new OutputStreamDelegate(connectionContainer, delegate, operationAccounting);
    }
    private Class[] getConnectionMemberInterfaces(Class clazz)  {
        Class[] oldArray = clazz.getInterfaces();
        if (oldArray.length == 0) {
            return null;
        }
        Class[] newArray = new Class[oldArray.length + 1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        newArray[oldArray.length] = ConnectionMember_I.class;
        return newArray;
    }
}
