package com.notionds.dataSupplier.delegation.refelction.logging;

import com.notionds.dataSupplier.NotionStartupException;
import com.notionds.dataSupplier.Container;
import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.exceptions.NotionExceptionWrapper;
import com.notionds.dataSupplier.operational.Operational;

import java.lang.reflect.Method;

public class ProxyLogging<N, O extends Operational, W extends Wrapper<N>, L extends ObjectProxyLogging<?,?,?>> extends com.notionds.dataSupplier.delegation.refelction.Proxy<N,O,W> {

    private final L dbLogging;
    private String description = "No description";

    public ProxyLogging(O operational, Container<N,O,W> container, N delegate, L dbLogging) {
        super(operational, container, delegate);
        this.dbLogging = dbLogging;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        InvokeAccounting invokeAccounting = this.getDbLogging().startInvoke(m, args);
        try {
            switch (m.getName()) {
                case "getDbLogging":
                    return this.getDbLogging();
                case "getDescription":
                    return this.getDescription();
                case "setDescription":
                    if (args[0] instanceof String) {
                        this.setDescription((String)args[0]);
                        return Void.TYPE;
                    }
                    throw new NotionStartupException(NotionStartupException.Type.ReflectiveOperationFailed, this.getClass());
            }
            return super.invoke(proxy, m, args);
        }
        catch (Throwable throwable) {
            if (invokeAccounting != null && throwable instanceof NotionExceptionWrapper) {
                this.getDbLogging().exception((NotionExceptionWrapper) throwable, this.description, m, invokeAccounting);
            }
            throw throwable;
        }
        finally {
            if (invokeAccounting != null) {
                if (args != null && args[0] != null && args[0] instanceof String) {
                    this.getDbLogging().endInvoke(m, (String)args[0], invokeAccounting);
                }
                else {
                    this.getDbLogging().endInvoke(m, description, invokeAccounting);
                }
            }
        }
    }

    public L getDbLogging() {
        return this.dbLogging;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
