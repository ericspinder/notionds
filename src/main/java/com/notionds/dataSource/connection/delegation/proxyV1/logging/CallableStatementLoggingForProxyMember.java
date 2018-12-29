package com.notionds.dataSource.connection.delegation.proxyV1.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.logging.CallableStatementLogging;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.util.UUID;

public class CallableStatementLoggingForProxyMember<O extends Options> extends CallableStatementLogging<O> {

    public CallableStatementLoggingForProxyMember(O options, final UUID connectionId, String sql) {
        super(options, connectionId, sql);
    }

    @Override
    public void executeInvoke() {}

    @Override
    public void startInvoke(Object proxy, Method m, Object[] args) {}

    @Override
    public void endInvoke() {}

    @Override
    public void exception(NotionExceptionWrapper notionExceptionWrapper) {}
}
