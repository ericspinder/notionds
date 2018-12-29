package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.logging.CallableStatementLogging;

import java.lang.reflect.Method;

public class CallableMember extends ProxyMember<CallableStatementLogging> {

    public CallableMember(ConnectionContainer connectionContainer, Object delegate, CallableStatementLogging callableStatementLogging) {
        super(connectionContainer, delegate, callableStatementLogging);
    }
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        if (m.getName().startsWith("execute")) {

        }
        return super.invoke(proxy, m, args);
    }
}
