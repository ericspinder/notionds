package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.jdbcProxy.WrapperFactory;

import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;

public class LoggingWrapperFactory extends WrapperFactory {

    private final LoggingService loggingService;

    public LoggingWrapperFactory(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <D> D getProxyMember(Class<?>[] interfaces, ConnectionContainer connectionContainer, D delegate, Object[] args) {
        if (delegate instanceof PreparedStatement) {
            return (D) Proxy.newProxyInstance(LoggingService.class.getClassLoader(), interfaces, new ProxyConnectionArtifactWithLogging<>(connectionContainer,delegate,this.loggingService, args));
        }
        return (D) Proxy.newProxyInstance(LoggingService.class.getClassLoader(), interfaces, new ProxyConnectionArtifactWithLogging<>(connectionContainer,delegate,this.loggingService));
    }
}
