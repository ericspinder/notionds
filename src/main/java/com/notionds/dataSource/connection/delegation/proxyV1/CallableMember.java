package com.notionds.dataSource.connection.delegation.proxyV1;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.accounting.CallableStatementAccounting;
import com.notionds.dataSource.connection.accounting.PreparedStatementAccounting;

import java.lang.reflect.Method;

public class CallableMember extends ProxyMember<CallableStatementAccounting> {

    public CallableMember(ConnectionContainer connectionContainer, Object delegate, CallableStatementAccounting callableStatementAccounting) {
        super(connectionContainer, delegate, callableStatementAccounting);
    }
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        switch (m.getName()) {
            case "execute":
                this.getOperationAccounting()..getCurrentSql();
                break;
            case "executeQuery":
                break;
            case "executeUpdate":
                break;
        }
        return super.invoke(proxy, m, args);
    }
}
