package com.notionds.dataSource.connection.delegation.proxyV1.withLog;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.logging.ResultsSetLogging;

import java.lang.reflect.Method;

public class ResultSetMemberWithLogging extends ProxyMemberWithLogging<ResultsSetLogging<?>> {

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
