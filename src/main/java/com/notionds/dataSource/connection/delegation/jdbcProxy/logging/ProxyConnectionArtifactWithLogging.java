package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ProxyConnectionArtifact;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

public class ProxyConnectionArtifactWithLogging<D> extends ProxyConnectionArtifact<D> {

    private static final Logger logger = LogManager.getLogger(ProxyConnectionArtifactWithLogging.class);
    private String sql;
    private final LoggingService loggingService;

    public ProxyConnectionArtifactWithLogging(ConnectionContainer connectionContainer, D delegate,LoggingService loggingService) {
        this(connectionContainer,delegate,loggingService,null);
    }
    public ProxyConnectionArtifactWithLogging(ConnectionContainer connectionContainer, D delegate,LoggingService loggingService,Object firstArg) {
        super(connectionContainer, delegate);
        this.loggingService = loggingService;
        if (firstArg instanceof String) {
            sql = (String) firstArg;
        }
        else {
            sql = null;
        }
    }

    @SuppressWarnings("unchecked")
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
