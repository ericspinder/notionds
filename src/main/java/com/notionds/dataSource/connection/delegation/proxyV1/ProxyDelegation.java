package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.accounting.CallableStatementAccounting;
import com.notionds.dataSource.connection.accounting.OperationAccounting;
import com.notionds.dataSource.connection.accounting.PreparedStatementAccounting;
import com.notionds.dataSource.connection.accounting.StatementAccounting;
import com.notionds.dataSource.connection.delegation.DelegationOfNotion;

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

    public ProxyDelegation(O options) {
        super(options);
    }

    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Object delegate, OperationAccounting operationAccounting) {
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        if (interfaces != null) {
            ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                    ProxyDelegation.class.getClassLoader(),
                    interfaces,
                    new ProxyMember(connectionContainer, delegate, operationAccounting));
            return connectionMember;
        }
        return null;

    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Statement delegate, StatementAccounting statementAccounting) {
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegation.class.getClassLoader(),
                interfaces,
                new StatementMember(connectionContainer, delegate, statementAccounting));
        return connectionMember;
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, PreparedStatement delegate, Class clazz, PreparedStatementAccounting preparedStatementAccounting) {
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegation.class.getClassLoader(),
                interfaces,
                new PreparedMember(connectionContainer, delegate, preparedStatementAccounting));
        return connectionMember;
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, CallableStatement delegate, Class clazz, CallableStatementAccounting callableStatementAccounting) {
        Class[] interfaces = this.getConnectionMemberInterfaces(delegate.getClass());
        ConnectionMember_I connectionMember = (ConnectionMember_I) Proxy.newProxyInstance(
                ProxyDelegation.class.getClassLoader(),
                interfaces,
                new CallableMember(connectionContainer, delegate, callableStatementAccounting));
        return connectionMember;
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

    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, InputStream delegate, OperationAccounting operationAccounting) {
        return new InputStreamDelegate(connectionContainer, delegate, operationAccounting);
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Reader delegate, OperationAccounting operationAccounting) {
        return new ReaderDelegate(connectionContainer, delegate, operationAccounting);
    }
    public ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, OutputStream delegate, OperationAccounting operationAccounting) {
        return new OutputStreamDelegate(connectionContainer, delegate, operationAccounting);
    }

}
