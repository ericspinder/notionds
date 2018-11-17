package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.accounting.StatementAccounting;

import java.lang.reflect.Method;

public class StatementMember extends ProxyMember<StatementAccounting> {

    public StatementMember(ConnectionContainer connectionContainer, Object delegate, StatementAccounting operationAccounting) {
        super(connectionContainer, delegate, operationAccounting);
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        switch (m.getName()) {

        }
        return super.invoke(proxy, m, args);
    }
}
