package com.notionds.dataSource.connection;

import com.notionds.dataSource.DatabaseMain;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.delegation.proxyV1.log.withLog.logging.CallableStatementLogging;
import com.notionds.dataSource.connection.delegation.proxyV1.log.withLog.logging.DbObjectLogging;
import com.notionds.dataSource.connection.delegation.proxyV1.log.withLog.logging.PreparedStatementLogging;
import com.notionds.dataSource.connection.delegation.proxyV1.log.withLog.logging.StatementLogging;
import com.notionds.dataSource.exceptions.ExceptionAdvice;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ConnectionAnalysis<O extends Options,
        DM extends DatabaseMain,
        OA extends DbObjectLogging,
        SA extends StatementLogging,
        PA extends PreparedStatementLogging,
        CA extends CallableStatementLogging> {

    private final O options;

    protected AtomicInteger exceptionCount;
    protected final DM databaseMain;
    private final Constructor<OA> dbObjectLoggingConstructor;
    private final Constructor<SA> statementLoggingConstructor;
    private final Constructor<PA> preparedStatementloggingConstructor;
    private final Constructor<CA> callableStatementLoggingConstructor;

    public ConnectionAnalysis(O options, DM databaseMain) {
        this.options = options;
        this.databaseMain = databaseMain;
        try {
            this.dbObjectLoggingConstructor = ((Class<OA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor(options.getClass(), UUID.class);
            this.statementLoggingConstructor = ((Class<SA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor(options.getClass(), UUID.class);
            this.preparedStatementloggingConstructor = ((Class<PA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor(options.getClass(), UUID.class, String.class);
            this.callableStatementLoggingConstructor = ((Class<CA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor(options.getClass(), UUID.class, String.class);
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("problem creating constructors for ConnectionAnalysis: " + roe.getMessage());
        }
    }
    public DbObjectLogging createAccounting(Class clazz, UUID connectionId, String maybeSql) {
        if (clazz.isAssignableFrom(Statement.class)) {
            if (clazz.isAssignableFrom(CallableStatement.class)) {
                return this.createCallableStatement(connectionId, maybeSql);
            }
            if (clazz.isAssignableFrom(PreparedStatement.class)) {
                return this.createPreparedStatement(connectionId, maybeSql);
            }
            return this.createStatementAccounting(connectionId);
        }
        return this.createOperationAccounting(connectionId);
    }

    protected OA createOperationAccounting(UUID connectionId) {
        try {
            return this.addOperationAccounting((OA)this.dbObjectLoggingConstructor.newInstance(connectionId));
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("problem creating instance of DbObjectLogging: " + roe.getMessage());
        }
    }
    protected abstract OA addOperationAccounting(OA operationAccounting);
    protected SA createStatementAccounting(UUID connectionId) {
        try {
            return this.addStatementAccounting((SA)this.statementLoggingConstructor.newInstance(connectionId));
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("problem creating instance of StatementLogging: " + roe.getMessage());
        }
    }
    protected abstract SA addStatementAccounting(SA statementAccounting);
    protected PA createPreparedStatement(UUID connectionId, String maybeSql) {
        try {
            return this.addPreparedStatementAccounting((PA)this.preparedStatementloggingConstructor.newInstance(connectionId, maybeSql));
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("problem creating instance of PreparedStatementLogging: " + roe.getMessage());
        }
    }
    protected abstract PA addPreparedStatementAccounting(PA preparedStatementAccounting);
    protected CA createCallableStatement(UUID connectionId, String maybeSql) {
        try {
            return this.addCallableStatementAccounting((CA) this.callableStatementLoggingConstructor.newInstance(connectionId, maybeSql));
        } catch (ReflectiveOperationException roe) {
            throw new RuntimeException("problem creating instance of CallableStatementLogging: " + roe.getMessage());
        }
    }
    protected abstract CA addCallableStatementAccounting(CA callableStatementAccounting);


    /**
     * Adds an logging of failures associated with the VendorConnection
     * processes for number of previously added failures and updates the operationAccounting object to state intention
     * if needed
     *
     * if Options.NotionDefaultIntegers.ConnectionAnalysis_Max_Exceptions is set to '0' then a single failure will trigger
     * a close of the underlying once the NotionPhantomReference Connection is in garbage collection
     *
     * @param exceptionWrapper
     * @return
     */
    public ExceptionAdvice.Recommendation reviewException(NotionExceptionWrapper exceptionWrapper) {

        if (options.get(Options.NotionDefaultIntegers.ConnectionAnalysis_Max_Exceptions).intValue() >= exceptionCount.addAndGet(1)) {
            return ExceptionAdvice.Recommendation.CloseConnectionInstance_When_Finished;
        }
        return this.addException(exceptionWrapper);
    }
    protected abstract ExceptionAdvice.Recommendation addException(NotionExceptionWrapper exceptionWrapper);
}
