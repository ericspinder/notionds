package com.notionds.dataSupplier.delegation.reflection.aggregation;

import com.notionds.dataSupplier.NotionStartupException;
import com.notionds.dataSupplier.Container;
import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.delegation.reflection.Proxy;
import com.notionds.dataSupplier.exceptions.NotionExceptionWrapper;
import com.notionds.dataSupplier.operational.Operational;

import java.lang.reflect.Method;

public class ProxyForAggregation<N, O extends Operational<N,W>, W extends Wrapper<N>, G extends InvokeAggregator, I extends InvokeInterceptor<N,O,W,G>> extends Proxy<N,O,W> {

    private final I invokeInterceptor;
    private String description = "No description";

    public ProxyForAggregation(N delegate, O operational, Container<N,O,W> container, I invokeInterceptor) {
        super(delegate, operational, container);
        this.invokeInterceptor = invokeInterceptor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        InvokeAccounting invokeAccounting = this.getInvokeInterceptor().startInvoke(m, args);
        try {
            switch (m.getName()) {
                case "getInvokeInterceptor":
                    return this.getInvokeInterceptor();
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
                this.getInvokeInterceptor().exception((NotionExceptionWrapper) throwable, this.description, m, invokeAccounting);
            }
            throw throwable;
        }
        finally {
            if (invokeAccounting != null) {
                if (args != null && args[0] != null && args[0] instanceof String) {
                    this.getInvokeInterceptor().endInvoke(m, (String)args[0], invokeAccounting);
                }
                else {
                    this.getInvokeInterceptor().endInvoke(m, description, invokeAccounting);
                }
            }
        }
    }

    public I getInvokeInterceptor() {
        return this.invokeInterceptor;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
