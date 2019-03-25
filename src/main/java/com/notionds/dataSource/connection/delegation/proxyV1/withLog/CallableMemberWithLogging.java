package com.notionds.dataSource.connection.delegation.proxyV1.withLog;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging.CallableStatementLogging;

import java.lang.reflect.Method;

public class CallableMemberWithLogging extends ProxyMemberWithLogging<CallableStatementLogging> {

    public CallableMemberWithLogging(ConnectionContainer connectionContainer, Object delegate, CallableStatementLogging callableStatementLogging) {
        super(connectionContainer, delegate, callableStatementLogging);
    }
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        if (m.getName().startsWith("execute")) {

        }
        return super.invoke(proxy, m, args);
    }
}
