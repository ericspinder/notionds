package com.notionds.dataSource.connection.delegation.proxyV1.withLog;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging.StatementLoggingForProxyMember;
import com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging.StatementLogging;

import java.lang.reflect.Method;

public class StatementMemberWithLogging<O extends Options> extends ProxyMemberWithLogging<O, StatementLogging<O>> {

    public StatementMemberWithLogging(ConnectionContainer connectionContainer, Object delegate, StatementLogging statementLogging) {
        super(connectionContainer, delegate, statementLogging);
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        switch (m.getName()) {

        }
        return super.invoke(proxy, m, args);
    }
}
