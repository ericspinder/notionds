package com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.logging.StatementLogging;

import java.util.UUID;

public class StatementLoggingForProxyMember<O extends Options> extends StatementLogging<O> {


    public StatementLoggingForProxyMember(O options, UUID connectionId) {
        super(options, connectionId);
    }


}
