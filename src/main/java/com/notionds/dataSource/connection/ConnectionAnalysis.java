package com.notionds.dataSource.connection;

import com.notionds.dataSource.ExceptionAdvice;
import com.notionds.dataSource.Options;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ConnectionAnalysis<O extends Options> {

    private final O options;
    private final Map<ExceptionAccounting, Boolean> exceptionMap = new ConcurrentHashMap<>();

    public ConnectionAnalysis(O options) {
        this.options = options;
    }

    /**
     * Adds an accounting of failures associated with the VendorConnection
     * processes for number of previously added failures and updates the exceptionAccounting object to state intention
     * if needed
     *
     * if Options.NotionDefaultIntegers.ConnectionAnalysis_Max_Exceptions is set to '0' then a single failure will trigger
     * a close of the underlying once the NotionConnection is in garbage collection
     *
     * @param exceptionAccounting
     * @return
     */
    public ExceptionAccounting addProblem(ExceptionAccounting exceptionAccounting) {
        if (options.get(Options.NotionDefaultIntegers.ConnectionAnalysis_Max_Exceptions).intValue() >= exceptionMap.size()) {
            exceptionAccounting.setRecommendation(ExceptionAdvice.Recommendation.CloseConnectionInstance_When_Finished);
            exceptionMap.put(exceptionAccounting, true);
        }
        else {
            exceptionMap.put(exceptionAccounting, false);
        }
        return exceptionAccounting;
    }

    /**
     * This map is to limit the number of SQL failures allowed for a connection
     * @return
     * key   ExceptionAccounting object
     * value true if the Exception had caused exceeded the max\
     *
     */
    public Map<ExceptionAccounting, Boolean> getExceptionMap() {
        return this.exceptionMap;
    }
}
