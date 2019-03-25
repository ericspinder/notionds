package com.notionds.dataSource.connection.delegation.proxyV1.withLog;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging.PreparedStatementLogging;

import java.lang.reflect.Method;

public class PreparedStatementMemberWithLogging extends ProxyMemberWithLogging<PreparedStatementLogging> {

    public PreparedStatementMemberWithLogging(ConnectionContainer connectionContainer, Object delegate, PreparedStatementLogging preparedStatementLogging) {
        super(connectionContainer, delegate, preparedStatementLogging);
    }
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        if (m.getName().startsWith("execute")) {
            this.getDbObjectLogging().executeStart();
            try {
                return m.invoke(proxy, args);
            }
            finally {
                this.getDbObjectLogging().executeEnd();
            }

        }
        return super.invoke(proxy, m, args);
    }
}
