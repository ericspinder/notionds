package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.exceptions.SqlExceptionWrapper;

import java.lang.reflect.Method;

public interface ProxyLogging_I<A extends InvokeAccounting> {
    A startInvoke(Method m, Object[] args);

    void exception(SqlExceptionWrapper sqlExceptionWrapper, Method m, A invokeAccounting);

    void endInvoke(Method m, Object[] args, A invokeAccounting);
}
