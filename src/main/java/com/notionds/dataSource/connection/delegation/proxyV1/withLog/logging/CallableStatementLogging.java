package com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging;

import com.notionds.dataSource.Options;

import java.util.UUID;

public abstract class CallableStatementLogging<O extends Options> extends PreparedStatementLogging<O> {

    public CallableStatementLogging(O options, UUID connectionId, String sql) {
        super(options, connectionId, sql);
    }
}
