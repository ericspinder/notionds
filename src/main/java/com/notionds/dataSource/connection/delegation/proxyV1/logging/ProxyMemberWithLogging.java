package com.notionds.dataSource.connection.delegation.proxyV1.logging;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.proxyV1.ProxyMember;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyMemberWithLogging<L extends Logging_forDbObject> extends ProxyMember implements InvocationHandler {

    private final L dbLogging;

    public ProxyMemberWithLogging(ConnectionContainer connectionContainer, Object delegate, L dbLogging) {
        super(connectionContainer, delegate);
        this.dbLogging = dbLogging;
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        InvokeAccounting invokeAccounting = this.getDbLogging().startInvoke(m, args);
        try {
            return super.invoke(proxy, m, args);
        }
        catch (Throwable throwable) {
            if (throwable instanceof NotionExceptionWrapper) {
                this.getDbLogging().exception((NotionExceptionWrapper) throwable, invokeAccounting);
            }
            throw throwable;
        }
        finally {
            this.getDbLogging().endInvoke(m, args, invokeAccounting);
        }
    }

    public L getDbLogging() {
        return this.dbLogging;
    }
}
