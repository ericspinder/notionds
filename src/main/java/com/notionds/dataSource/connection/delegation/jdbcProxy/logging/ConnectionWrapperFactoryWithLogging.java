package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.Container;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ConnectionWrapperFactory;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ProxyConnectionArtifact;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class ConnectionWrapperFactoryWithLogging<DL extends ObjectProxyLogging<?,?>, SL extends StatementLogging<?,?>, PL extends PreparedStatementLogging<?,?>> extends ConnectionWrapperFactory {

    private static final Logger logger = LogManager.getLogger(ConnectionWrapperFactoryWithLogging.class);

    public static class Default extends ConnectionWrapperFactoryWithLogging<ObjectProxyLogging.Default<?>, StatementLogging.Default<?>, PreparedStatementLogging.Default<?>> {
        public static final ConnectionWrapperFactoryWithLogging.Default INSTANCE = new ConnectionWrapperFactoryWithLogging.Default();

        public Default() {
            super(NotionDs.DEFAULT_OPTIONS_INSTANCE, LoggingService.Default.INSTANCE);
        }
    }

    private final LoggingService<?,?,?,?,?> loggingService;

    public ConnectionWrapperFactoryWithLogging(Options options, LoggingService<?,?,?,?,?> loggingService) {
        super(options);
        this.loggingService = loggingService;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <D> ProxyConnectionArtifact<D> createProxyMember(Container container, D delegate, Object[] args) {
        Options.Option<Boolean> logNonExecute = this.options.get(Options.NotionBooleans.LogNonExecuteProxyMembers.getKey());
        if (delegate instanceof PreparedStatement) {
            return new ProxyWithLoggingConnectionArtifact<D, PL>(container, delegate, (PL) this.loggingService.newPreparedStatementLogging((String) args[0]));
        }
        else if (delegate instanceof Statement){
            return new ProxyWithLoggingConnectionArtifact<D, SL>(container, delegate, (SL) this.loggingService.newStatementLogging());
        }
        else if (logNonExecute.getValue()) {
            return new ProxyWithLoggingConnectionArtifact<D, DL>(container, delegate, (DL) this.loggingService.newObjectProxyLogging());
        }
        else {
            return super.createProxyMember(container, delegate, args);
        }
    }

}
