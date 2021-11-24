package com.notionds.dataSupplier.exceptions;

import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.operational.Operational;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public abstract class Advice<N, O extends Operational, W extends Wrapper<N>> {

    private static final Logger logger = LogManager.getLogger(Advice.class);

    private class ExceptionCounterByDuration {
        private LinkedHashMap<Instant, Boolean> linkedHashMap = new LinkedHashMap<Instant, Boolean>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Instant, Boolean> eldest) {
                return Instant.now().minus(qualifyingTime).isAfter(eldest.getKey());
            }
        };
        private final Duration qualifyingTime;
        private final int numberOfOccurrences;

        public ExceptionCounterByDuration(Duration qualifyingTime, int numberOfOccurrences) {
            this.qualifyingTime = qualifyingTime;
            this.numberOfOccurrences = numberOfOccurrences;
        }
        public Boolean addOne() {
            if (this.numberOfOccurrences > linkedHashMap.size()) {
                this.linkedHashMap.put(Instant.now(), true);
                return true;
            }
            this.linkedHashMap.put(Instant.now(), false);
            return false;
        }
    }
    protected final O options;

    protected abstract Recommendation parseSqlState(String sqlState);
    protected abstract Recommendation addSocketTimeout();
    protected abstract Recommendation examineMessage(String message);

    public Advice(O options) {
        this.options = options;
    }

    protected Recommendation examine(Throwable throwable, List<Recommendation> previousRecommendations) {
        if (throwable instanceof SQLException) {
            previousRecommendations.add(parseSqlState(((SQLException) throwable).getSQLState()));
        }
        if (throwable instanceof java.net.SocketTimeoutException) {
            previousRecommendations.add(addSocketTimeout());
        }
        previousRecommendations.add(examineMessage(throwable.getMessage()));
        if (throwable.getCause() != null) {
            return this.examine(throwable.getCause(), previousRecommendations);
        }
        return findMostSerious(previousRecommendations);
    }
    protected Recommendation findMostSerious(List<Recommendation> previousRecommendations) {
        if (Recommendation.Authentication_Failover.isInCollection(previousRecommendations)) {
            return Recommendation.Authentication_Failover;
        }
        if (Recommendation.Database_Failover.isInCollection(previousRecommendations)) {
            return Recommendation.Database_Failover;
        }
        for (Recommendation recommendation :previousRecommendations) {
            if (recommendation.isFailoverToNextConnectionSupplier()) {
                return recommendation;
            }
        }
        return Recommendation.Close_Closable;
    }

    public SqlExceptionWrapper wrapAdviseSqlException(SQLException sqlException) {
        StringBuilder s = new StringBuilder();
        s.append("NotionDs wrapped SQLException, recommendation=");
        try {
            Recommendation recommendation = this.examine(sqlException, new ArrayList<>());
            s.append(recommendation);
            return new SqlExceptionWrapper(s.toString(), sqlException, recommendation);
        }
        finally {
            if (logger.isDebugEnabled()) {
                s.append('\n').append(sqlException);
            }
            logger.info(s.toString());
        }
    }
    public SqlClientInfoExceptionWrapper adviseSQLClientInfoException(SQLClientInfoException sqlClientInfoException) {
        StringBuilder s = new StringBuilder();
        s.append("NotionDs wrapped SQLClientInfoException, recommendation=");
        try {
            Recommendation recommendation = this.examine(sqlClientInfoException, new ArrayList<>());
            s.append(recommendation);
            return new SqlClientInfoExceptionWrapper(s.toString(), recommendation, sqlClientInfoException);
        }
        finally {
            if (logger.isDebugEnabled()) {
                s.append('\n').append(sqlClientInfoException);
            }
            logger.info(s.toString());
        }
    }
    public IoExceptionWrapper adviseIoException(IOException ioException) {
        StringBuilder s = new StringBuilder();
        s.append("NotionDs wrapped IOException, recommendation=");
        try {
            Recommendation recommendation = this.examine(ioException, new ArrayList<>());
            s.append(recommendation);
            return new IoExceptionWrapper(s.toString(), recommendation, ioException);
        }
        finally {
            if (logger.isDebugEnabled()) {
                s.append('\n').append(ioException);
            }
            logger.info(s.toString());
        }
    }
    public ExceptionWrapper adviseException(Exception exception) {
        StringBuilder s = new StringBuilder();
        s.append("NotionDs wrapped Exception, recommendation=");
        try {
            Recommendation recommendation = this.examine(exception, new ArrayList<>());
            s.append(recommendation);
            return new ExceptionWrapper(s.toString(), recommendation, exception);
        }
        finally {
            if (logger.isDebugEnabled()) {
                s.append('\n').append(exception);
            }
            logger.info(s.toString());
        }
    }
}
