package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.Container;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ConnectionWrapperFactory;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ProxyConnectionArtifact;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class ConnectionWrapperFactoryWithLogging<O extends Options, DL extends ObjectProxyLogging<?, ?, ?>, SL extends StatementLogging<?, ?,?>, PL extends PreparedStatementLogging<?, ? ,?>> extends ConnectionWrapperFactory<O> {

    private static final Logger logger = LogManager.getLogger(ConnectionWrapperFactoryWithLogging.class);

    public static final ConnectionWrapperFactoryWithLogging.Default DEFAULT_INSTANCE = new ConnectionWrapperFactoryWithLogging.Default();

    public static class Default extends ConnectionWrapperFactoryWithLogging<Options.Default, ObjectProxyLogging.Default<?>, StatementLogging.Default<?>, PreparedStatementLogging.Default<?>> {

        public Default() {
            super(Options.DEFAULT_OPTIONS_INSTANCE, Analysis.DEFAULT_INSTANCE);
        }
    }

    private final Analysis<?,?,?,?,?> analysis;

    public ConnectionWrapperFactoryWithLogging(O options, Analysis<?,?,?,?,?> analysis) {
        super(options);
        this.analysis = analysis;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <D> ProxyConnectionArtifact<D> createProxyMember(Container<?,?,?> container, D delegate, Object[] args) {
        logger.debug("createProxyMember for " + delegate.getClass().getCanonicalName());
        Options.Option<Boolean> logNonExecute = this.options.get(Options.NotionDefaultBooleans.LogNonExecuteProxyMembers.getKey());
        if (delegate instanceof PreparedStatement) {
            return new ProxyWithLoggingConnectionArtifact<D, PL>(container, delegate, (PL) this.analysis.newPreparedStatementLogging(container.containerId, (String) args[0]));
        }
        else if (delegate instanceof Statement){
            return new ProxyWithLoggingConnectionArtifact<D, SL>(container, delegate, (SL) this.analysis.newStatementLogging(container.containerId));
        }
        else if (logNonExecute.getValue()) {
            return new ProxyWithLoggingConnectionArtifact<D, DL>(container, delegate, (DL) this.analysis.newObjectProxyLogging(container.containerId));
        }
        else {
            return super.createProxyMember(container, delegate, args);
        }
    }

}
