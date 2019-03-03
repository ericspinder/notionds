package com.notionds.dataSource.connection.delegation.proxyV1.withLog;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.logging.StatementLogging;

import java.lang.reflect.Method;

public class StatementMemberWithLogging extends ProxyMemberWithLogging<StatementLogging> {

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
