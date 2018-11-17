package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.accounting.OperationAccounting;
import com.notionds.dataSource.connection.accounting.PreparedStatementAccounting;

import java.lang.reflect.Method;

public class PreparedMember extends ProxyMember<PreparedStatementAccounting> {

    public PreparedMember(ConnectionContainer connectionContainer, Object delegate, PreparedStatementAccounting operationAccounting) {
        super(connectionContainer, delegate, operationAccounting);
    }
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        switch (m.getName()) {
            case "execute":
                this.getOperationAccounting().getCurrentSql();
                break;
            case "executeQuery":
                break;
            case "executeUpdate":
                break;
        }
        return super.invoke(proxy, m, args);
    }
}
