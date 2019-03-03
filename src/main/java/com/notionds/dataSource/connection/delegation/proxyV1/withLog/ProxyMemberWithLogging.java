package com.notionds.dataSource.connection.delegation.proxyV1.withLog;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
import com.notionds.dataSource.connection.delegation.proxyV1.ProxyMember;
import com.notionds.dataSource.connection.logging.DbObjectLogging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyMemberWithLogging<L extends DbObjectLogging> extends ProxyMember implements InvocationHandler {

    private final L dbLogging;

    public ProxyMemberWithLogging(ConnectionContainer connectionContainer, Object delegate, L dbLogging) {
        super(connectionContainer, delegate);
        this.dbLogging = dbLogging;
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        this.getDbLogging().startInvoke(proxy, m, args);
        try {
            return super.invoke(proxy, m, args);
        }
        finally {
            this.getDbLogging().endInvoke();
        }
    }

    public L getDbLogging() {
        return this.dbLogging;
    }
}
