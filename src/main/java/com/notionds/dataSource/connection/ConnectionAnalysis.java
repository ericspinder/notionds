package com.notionds.dataSource.connection;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.ExceptionAdvice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ConnectionAnalysis<O extends Options> {

    private final O options;
    private final Map<OperationAccounting, State> exceptionMap = new ConcurrentHashMap<>();
    private final Map<OperationAccounting, State> operationMap = new ConcurrentHashMap<>();

    public ConnectionAnalysis(O options) {
        this.options = options;
    }


    public OperationAccounting addOperation(OperationAccounting operationAccounting, ConnectionMember_I delegatedInstance) {
        if (options.get(Options.NotionDefaultIntegers.ConnectionAnalysis_Max_Normal_Seconds) >= operationAccounting.getDuration().toSeconds()) {

        }
        this.operationMap.put(operationAccounting, delegatedInstance.getState());
        return operationAccounting;
    }

    /**
     * Adds an accounting of failures associated with the VendorConnection
     * processes for number of previously added failures and updates the operationAccounting object to state intention
     * if needed
     *
     * if Options.NotionDefaultIntegers.ConnectionAnalysis_Max_Exceptions is set to '0' then a single failure will trigger
     * a close of the underlying once the NotionConnection_Keep_for_now is in garbage collection
     *
     * @param operationAccounting
     * @return
     */
    public OperationAccounting addException(OperationAccounting operationAccounting, ConnectionMember_I delegatedInstance) {
        if (options.get(Options.NotionDefaultIntegers.ConnectionAnalysis_Max_Exceptions).intValue() >= exceptionMap.size()) {
            operationAccounting.setRecommendation(ExceptionAdvice.Recommendation.CloseConnectionInstance_When_Finished);
        }
        exceptionMap.put(operationAccounting, delegatedInstance.getState());
        return operationAccounting;
    }

    /**
     * This map is to limit the number of SQL failures allowed for a connection
     * @return
     * key   OperationAccounting object
     * value true if the Exception had caused exceeded the max\
     *
     */
    public Map<OperationAccounting, State> getExceptionMap() {
        return this.exceptionMap;
    }
}
