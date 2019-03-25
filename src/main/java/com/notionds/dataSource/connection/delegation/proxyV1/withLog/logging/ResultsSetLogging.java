package com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging;

import com.notionds.dataSource.Options;

import java.util.UUID;

public abstract  class ResultsSetLogging<O extends Options> extends DbObjectLogging<O> {


    public ResultsSetLogging(O options, UUID connectionId) {
        super(options, connectionId);
    }
    public abstract void resultsSetStart();
    public abstract  void resultsSetStop();
}
