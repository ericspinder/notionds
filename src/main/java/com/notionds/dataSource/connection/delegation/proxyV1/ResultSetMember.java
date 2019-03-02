package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.logging.ResultsSetLogging;

import java.lang.reflect.Method;

public class ResultSetMember extends ProxyMember<ResultsSetLogging<?>> {

    public ResultSetMember(ConnectionContainer connectionContainer, Object delegate, ResultsSetLogging resultsSetLogging) {
        super(connectionContainer, delegate, resultsSetLogging);
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        switch (m.getName()) {

        }
        return super.invoke(proxy, m, args);
    }
}
