package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.logging.PreparedStatementLogging;

import java.lang.reflect.Method;

public class PreparedMember extends ProxyMember<PreparedStatementLogging> {

    public PreparedMember(ConnectionContainer connectionContainer, Object delegate, PreparedStatementLogging preparedStatementLogging) {
        super(connectionContainer, delegate, preparedStatementLogging);
    }
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        if (m.getName().startsWith("execute")) {

        }
        return super.invoke(proxy, m, args);
    }
}
