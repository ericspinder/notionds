package com.notionds.dataSource.connection;

import com.notionds.dataSource.*;
import com.notionds.dataSource.connection.accounting.CallableStatementAccounting;
import com.notionds.dataSource.connection.accounting.OperationAccounting;
import com.notionds.dataSource.connection.accounting.PreparedStatementAccounting;
import com.notionds.dataSource.connection.accounting.StatementAccounting;
import com.notionds.dataSource.exceptions.ExceptionAdvice;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ConnectionAnalysis<O extends Options,
        DM extends DatabaseMain,
        OA extends OperationAccounting,
        SA extends StatementAccounting,
        PA extends PreparedStatementAccounting,
        CA extends CallableStatementAccounting> {

    private final O options;

    protected AtomicInteger exceptionCount;
    protected final DM databaseMain;
    private final Constructor<OA> operationAccountingConstructor;
    private final Constructor<SA> statementAccountingConstructor;
    private final Constructor<PA> preparedStatementAccountingConstructor;
    private final Constructor<CA> callableStatementAccountingConstructor;

    public ConnectionAnalysis(O options, DM databaseMain) {
        this.options = options;
        this.databaseMain = databaseMain;
        try {
            this.operationAccountingConstructor = ((Class<OA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor(UUID.class);
            this.statementAccountingConstructor = ((Class<SA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor(UUID.class);
            this.preparedStatementAccountingConstructor = ((Class<PA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor(UUID.class, String.class);
            this.callableStatementAccountingConstructor = ((Class<CA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor(UUID.class, String.class);
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("problem creating constructors for ConnectionAnalysis: " + roe.getMessage());
        }
    }
    public OperationAccounting createAccounting(Class clazz, UUID connectionId, String maybeSql) {
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
            return this.addOperationAccounting((OA)this.operationAccountingConstructor.newInstance(connectionId));
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("problem creating instance of OperationAccounting: " + roe.getMessage());
        }
    }
    protected abstract OA addOperationAccounting(OA operationAccounting);
    protected SA createStatementAccounting(UUID connectionId) {
        try {
            return this.addStatementAccounting((SA)this.statementAccountingConstructor.newInstance(connectionId));
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("problem creating instance of StatementAccounting: " + roe.getMessage());
        }
    }
    protected abstract SA addStatementAccounting(SA statementAccounting);
    protected PA createPreparedStatement(UUID connectionId, String maybeSql) {
        try {
            return this.addPreparedStatementAccounting((PA)this.preparedStatementAccountingConstructor.newInstance(connectionId, maybeSql));
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("problem creating instance of PreparedStatementAccounting: " + roe.getMessage());
        }
    }
    protected abstract PA addPreparedStatementAccounting(PA preparedStatementAccounting);
    protected CA createCallableStatement(UUID connectionId, String maybeSql) {
        try {
            return this.addCallableStatementAccounting((CA) this.callableStatementAccountingConstructor.newInstance(connectionId, maybeSql));
        } catch (ReflectiveOperationException roe) {
            throw new RuntimeException("problem creating instance of CallableStatementAccounting: " + roe.getMessage());
        }
    }
    protected abstract CA addCallableStatementAccounting(CA callableStatementAccounting);


    /**
     * Adds an accounting of failures associated with the VendorConnection
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
