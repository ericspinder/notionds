package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.jdbcProxy.WrapperFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class LoggingWrapperFactory extends WrapperFactory {

    private static final Logger logger = LogManager.getLogger(LoggingWrapperFactory.class);

    private final LoggingService loggingService;

    public LoggingWrapperFactory(LoggingService loggingService) {
        this.loggingService = loggingService;
    }


    @Override
    @SuppressWarnings("unchecked")
    protected <D> ConnectionArtifact_I<D> createProxyMember(ConnectionContainer connectionContainer, D delegate, Object[] args) {
        if (delegate instanceof PreparedStatement) {
            return new WithLoggingProxyConnection<>(connectionContainer, delegate, this.loggingService.newPreparedStatementLogging(connectionContainer.getConnectionPool().getOptions(), (String) args[0]));
        }
        else if (delegate instanceof Statement){
            return new WithLoggingProxyConnection<>(connectionContainer, delegate, this.loggingService.newStatementLogging(connectionContainer.getConnectionPool().getOptions()));
        }
        else {
            return new WithLoggingProxyConnection<>(connectionContainer, delegate, this.loggingService.newObjectProxyLogging(connectionContainer.getConnectionPool().getOptions()));
        }
    }
}
