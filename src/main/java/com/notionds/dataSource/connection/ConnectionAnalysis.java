package com.notionds.dataSource.connection;

import com.notionds.dataSource.*;
import com.notionds.dataSource.connection.accounting.CallableStatementAccounting;
import com.notionds.dataSource.connection.accounting.OperationAccounting;
import com.notionds.dataSource.connection.accounting.PreparedStatementAccounting;
import com.notionds.dataSource.connection.accounting.StatementAccounting;
import com.notionds.dataSource.exceptions.ExceptionAdvice;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ConnectionAnalysis<O extends Options, DM extends DatabaseMain> {

    private final O options;

    protected AtomicInteger exceptionCount;
    protected final DM databaseMain;

    public ConnectionAnalysis(O options, DM databaseMain) {
        this.options = options;
        this.databaseMain = databaseMain;
    }

    public abstract StatementAccounting addStatement(StatementAccounting statementAccounting);
    public abstract CallableStatementAccounting addCallableStatement(CallableStatementAccounting callableStatementAccounting);
    public abstract PreparedStatementAccounting addPreparedStatement(PreparedStatementAccounting preparedStatementAccounting);
    protected abstract OperationAccounting addOperation(OperationAccounting operationAccounting, ConnectionMember_I delegatedInstance);
    protected abstract ExceptionAdvice.Recommendation addException(Exception exception);

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
    public ExceptionAdvice.Recommendation analyizeException(NotionExceptionWrapper exceptionWrapper) {

        if (options.get(Options.NotionDefaultIntegers.ConnectionAnalysis_Max_Exceptions).intValue() >= exceptionCount.addAndGet(1)) {
            return ExceptionAdvice.Recommendation.CloseConnectionInstance_When_Finished;
        }

    }
}
