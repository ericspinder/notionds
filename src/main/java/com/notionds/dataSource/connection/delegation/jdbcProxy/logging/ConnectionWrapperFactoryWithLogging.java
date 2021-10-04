package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ConnectionWrapperFactory;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ProxyConnectionArtifact;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;

public class ConnectionWrapperFactoryWithLogging<O extends Options, IA extends InvokeAccounting, IG extends InvokeAggregator<O, IA>, IL extends InvokeLibrary<O, IA, IG>, DL extends Logging_forDbObject<O, IA, IG, IL>, SL extends Logging_forStatement<O, IA, IG, IL>, PL extends Logging_forPreparedStatement<O, IA, IG, IL>> extends ConnectionWrapperFactory<O> {

    private static final Logger logger = LogManager.getLogger(ConnectionWrapperFactoryWithLogging.class);

    private final IL invokeLibrary;
    protected final Class<IL> invokeLibraryClass;
    protected final Class<DL> dbObjectLoggingClass;
    protected final Class<SL> statementLoggingClass;
    protected final Class<PL> preparedStatementLoggingClass;
    protected final Constructor<IL> invokeLibraryConstructor;
    protected final Constructor<DL> dbObjectLoggingConstructor;
    protected final Constructor<SL> statementLoggingConstructor;
    protected final Constructor<PL> preparedStatementLoggingConstructor;

    public ConnectionWrapperFactoryWithLogging(O options) {
        super(options);
        this.invokeLibraryClass = (Class<IL>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[3];
        this.dbObjectLoggingClass = (Class<DL>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.statementLoggingClass = (Class<SL>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.preparedStatementLoggingClass = (Class<PL>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[2];
        try {
            this.invokeLibraryConstructor = invokeLibraryClass.getDeclaredConstructor(Options.class);
            this.invokeLibrary = this.invokeLibraryConstructor.newInstance(options);
            this.dbObjectLoggingConstructor = dbObjectLoggingClass.getDeclaredConstructor(Options.class, UUID.class, invokeLibraryClass);
            this.statementLoggingConstructor = statementLoggingClass.getDeclaredConstructor(Options.class, UUID.class, invokeLibraryClass);
            this.preparedStatementLoggingConstructor = preparedStatementLoggingClass.getDeclaredConstructor(Options.class, UUID.class, invokeLibraryClass, String.class);
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }


    @Override
    public ProxyConnectionArtifact createProxyMember(ConnectionContainer connectionContainer, Object delegate, Object[] args) {
        logger.trace("createProxyMember(...Object....");
        if (this.options.get(Options.NotionDefaultBooleans.LogNonExecuteProxyMembers)) {
            try {
                DL dbObjectLogging = this.dbObjectLoggingConstructor.newInstance(this.options, connectionContainer.getConnectionId(), this.invokeLibrary);
                return new ProxyWithLoggingConnectionArtifact<>(connectionContainer, delegate, dbObjectLogging);
            }
            catch (ReflectiveOperationException roe) {
                throw new RuntimeException(roe);
            }
        }
        else {
            return super.createProxyMember(connectionContainer, delegate, args);
        }
    }
    public ProxyConnectionArtifact createProxyMember(ConnectionContainer connectionContainer, Statement delegate, Object[] args) {
        logger.trace("createProxyMember(...Statement...)");
        try {
            SL loggingForStatement = this.statementLoggingConstructor.newInstance(this.options, connectionContainer.getConnectionId(), this.invokeLibrary);
            return new ProxyWithLoggingConnectionArtifact<>(connectionContainer, delegate, loggingForStatement);
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }

    public ProxyConnectionArtifact createProxyMember(ConnectionContainer connectionContainer, PreparedStatement delegate, Object[] args) {
        logger.trace("createProxyMember(...PreparedStatement....");
        if (args != null && args[0] instanceof String) {
            try {
                PL preparedStatementLogging = this.preparedStatementLoggingConstructor.newInstance(this.options, connectionContainer.getConnectionId(), this.invokeLibrary, (String) args[0]);
                return new ProxyWithLoggingConnectionArtifact<>(connectionContainer, delegate,preparedStatementLogging);
            }
            catch (ReflectiveOperationException roe) {
                throw new RuntimeException(roe);
            }
        }
        else {
            logger.error("prepared statement created without a sql");
            return super.createProxyMember(connectionContainer, delegate, args);
        }
    }
}
