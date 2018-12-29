package com.notionds.dataSource.connection.delegation.proxyV1.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.logging.StatementLogging;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;
import java.util.UUID;

public class StatementLoggingForProxyMember<O extends Options> extends StatementLogging<O> {


    public StatementLoggingForProxyMember(O options, UUID connectionId) {
        super(options, connectionId);
    }

    @Override
    public void executeInvoke(Object proxy, Method m, Object[] args) {}

    @Override
    public void startInvoke(Object proxy, Method m, Object[] args) {}

    @Override
    public void endInvoke() {}

    @Override
    public void exception(NotionExceptionWrapper notionExceptionWrapper) {}
}
