package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.jdbcProxy.WrapperFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;

public class LoggingWrapperFactory extends WrapperFactory {

    private static final Logger logger = LogManager.getLogger(LoggingWrapperFactory.class);

    private final LoggingService loggingService;

    public LoggingWrapperFactory(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <D> ConnectionArtifact_I<D> getProxyMember(Class<?>[] interfaces, ConnectionContainer connectionContainer, D delegate, Object[] args) {
        if (delegate instanceof PreparedStatement) {
            return (ConnectionArtifact_I<D>) Proxy.newProxyInstance(LoggingService.class.getClassLoader(), interfaces, new ProxyConnectionArtifactWithLogging<>(connectionContainer,delegate,this.loggingService, args[0]));
        }
        return (ConnectionArtifact_I<D>) Proxy.newProxyInstance(LoggingService.class.getClassLoader(), interfaces, new ProxyConnectionArtifactWithLogging<>(connectionContainer,delegate,this.loggingService));
    }
}
