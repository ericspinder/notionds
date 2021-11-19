package com.notionds.dataSupplier.jdbc;

import com.notionds.dataSupplier.pool.Pool;
import com.notionds.dataSupplier.Container;
import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.exceptions.Recommendation;
import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.delegation.Delegation;
import com.notionds.dataSupplier.exceptions.*;

import java.sql.*;

public class JdbcContainer<N,O extends Operational,W extends Wrapper<N> W extends Delegation> extends Container<O, P, A, W> implements Comparable<JdbcContainer> {

    public JdbcContainer(O options, P connectionPool, A exceptionAdvice, W artifactWrapper) {
        super(options, connectionPool, exceptionAdvice, artifactWrapper);
    }
    @SuppressWarnings("unchecked")

    @Override
    protected boolean reuseInterest(Wrapper artifact, boolean currently) {
        return currently;
    }

    public SQLException handleSQLException(SQLException sqlException, Wrapper delegatedInstance) {
        logger.debug(sqlException.getMessage());
        SqlExceptionWrapper sqlExceptionWrapped = this.advice.wrapAdviseSqlException(sqlException);
        this.reviewException(delegatedInstance, sqlExceptionWrapped.getRecommendation());
        return sqlExceptionWrapped;
    }

    public SQLClientInfoException handleSQLClientInfoException(SQLClientInfoException sqlClientInfoException, Wrapper delegatedInstance) {
        logger.debug(sqlClientInfoException.getMessage());
        SqlClientInfoExceptionWrapper sqlClientInfoExceptionWrapper = this.advice.adviseSQLClientInfoException(sqlClientInfoException);
        this.reviewException(delegatedInstance, sqlClientInfoExceptionWrapper.getRecommendation());
        return sqlClientInfoExceptionWrapper;
    }



    @Override


    @Override
    public int compareTo(JdbcContainer that) {
        if (this.containerId.equals(that.containerId)) {
            return 0;
        } else {
            return (this.createInstant.compareTo(that.createInstant) == 0) ?
                    this.containerId.compareTo(that.containerId) :
                    this.createInstant.compareTo(that.createInstant);
        }
    }

    @Override
    public void reviewException(Wrapper connectionArtifact, Recommendation recommendation) {
        if (recommendation.shouldClose() && connectionArtifact != null) {
            logger.debug("Close delegate");
            this.closeDelegate(connectionArtifact);
        }
        if (recommendation.isFailoverToNextConnectionSupplier()) {
            logger.info("Failover");
            this.pool.doFailover(recommendation);
        }
    }
}
