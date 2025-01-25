package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.NotionStartupException;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ProxyConnectionArtifact;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

public class ProxyConnectionArtifactWithLogging<D> extends ProxyConnectionArtifact<D> {

    private static final Logger logger = LogManager.getLogger(ProxyConnectionArtifactWithLogging.class);
    private final ObjectProxyLogging dbLogging;
    private String description = "No description";

    public ProxyConnectionArtifactWithLogging(ConnectionContainer connectionContainer, D delegate, ObjectProxyLogging dbLogging) {
        super(connectionContainer, delegate);
        this.dbLogging = dbLogging;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        InvokeAccounting invokeAccounting = this.getDbLogging().startInvoke(m, args);
        try {
            return switch (m.getName()) {
                case "getDbLogging" -> this.getDbLogging();
                case "getDescription" -> this.getDescription();
                case "setDescription" -> {
                    if (args[0] instanceof String) {
                        this.setDescription((String) args[0]);
                        yield Void.TYPE;
                    }
                    throw new NotionStartupException(NotionStartupException.Type.ReflectiveOperationFailed, this.getClass());
                }
                default -> super.invoke(proxy, m, args);
            };
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
                    this.getDbLogging().endInvoke(m,"-", invokeAccounting);
                }
            }
        }
    }

    public ObjectProxyLogging getDbLogging() {
        return this.dbLogging;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
