package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ProxyConnectionArtifact;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Method;

public class ProxyConnectionArtifactWithLogging<D> extends ProxyConnectionArtifact<D> {

    private String sql;
    private final LoggingService loggingService;

    public ProxyConnectionArtifactWithLogging(ConnectionContainer connectionContainer, D delegate, LoggingService loggingService) {
        this(connectionContainer,delegate,loggingService, (Object) null);
    }
    public ProxyConnectionArtifactWithLogging(ConnectionContainer connectionContainer, D delegate, LoggingService loggingService, Object... args) {
        super(connectionContainer, delegate);
        this.loggingService = loggingService;
        if (args != null && args[0] instanceof String) {
            sql = (String) args[0];
        }
        else {
            sql = null;
        }
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        InvokeAccounting invokeAccounting = this.loggingService.startInvoke(m, args, this.sql);
        try {
            return switch (m.getName()) {
                case "getLoggingService" -> this.loggingService;
                case "getSQL" -> this.sql;
                case "addBatch" -> {
                    this.sql = (String) args[0];
                    yield super.invoke(proxy,m,args);
                }
                default -> super.invoke(proxy, m, args);
            };
        }
        catch (Throwable throwable) {
            if (invokeAccounting != null && throwable instanceof NotionExceptionWrapper) {
                this.loggingService.populateThrownException((NotionExceptionWrapper) throwable, invokeAccounting);
            }
            throw throwable;
        }
        finally {
            if (invokeAccounting != null) {
                this.loggingService.populateExecution(invokeAccounting);
            }
        }
    }
}
