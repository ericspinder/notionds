package com.notionds.dataSource.connection.delegation.proxyV1.withLog;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.proxyV1.withLog.logging.ResultsSetLogging;

import java.lang.reflect.Method;

public class ResultSetMemberWithLogging<O extends Options> extends ProxyMemberWithLogging<O, ResultsSetLogging<O>> {

    public ResultSetMemberWithLogging(ConnectionContainer connectionContainer, Object delegate, ResultsSetLogging resultsSetLogging) {
        super(connectionContainer, delegate, resultsSetLogging);
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        switch (m.getName()) {

        }
        return super.invoke(proxy, m, args);
    }
}
