package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class ExceptionAdvice<O extends Options> {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    public static class KillExceptionOnException<O extends Options> extends ExceptionAdvice<O> {

        private Logger logger = LoggerFactory.getLogger(KillExceptionOnException.class);

        public KillExceptionOnException(O options) {
            super(options);
        }

        @Override
        protected Recommendation parseSQLException(SQLException sqlException) {
            logger.error(sqlException.getMessage());
            return Recommendation.CloseConnectionInstance_When_Finished;
        }

        @Override
        protected Recommendation parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException) {
            logger.error(sqlClientInfoException.getMessage());
            return Recommendation.CloseConnectionInstance_When_Finished;
        }

        @Override
        protected Recommendation parseIOException(IOException ioException) {
            logger.error(ioException.getMessage());
            return Recommendation.CloseConnectionInstance_When_Finished;
        }

        @Override
        protected Recommendation parseException(Exception exception) {
            logger.error(exception.getMessage());
            return Recommendation.CloseConnectionInstance_When_Finished;
        }

        @Override
        protected String descriptionOfExceptionAdvice() {
            return "Default Close Connection on all Exceptions";
        }
    }

    protected final O options;

    public ExceptionAdvice(O options) {
        this.options = options;
    }

    protected abstract String descriptionOfExceptionAdvice();
    protected abstract Recommendation parseSQLException(SQLException sqlException);
    protected abstract Recommendation parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException);
    protected abstract Recommendation parseIOException(IOException ioException);
    protected abstract Recommendation parseException(Exception exception);

    public SqlExceptionWrapper adviseSqlException(SQLException sqlException) {
        Recommendation recommendation = this.parseSQLException(sqlException);
        logger.error("Recommended={} for SQLException={}, using ExceptionAdvice={}", recommendation.getDescription(), sqlException.getMessage(), this.descriptionOfExceptionAdvice());
        return new SqlExceptionWrapper(recommendation, sqlException);
    }
    public SqlClientInfoExceptionWrapper adviseSQLClientInfoException(SQLClientInfoException sqlClientInfoException) {
        Recommendation recommendation = this.parseSQLClientInfoException(sqlClientInfoException);
        logger.error("Recommended={} for SQLClientInfoException={}, using ExceptionAdvice={}", recommendation.getDescription(), sqlClientInfoException.getMessage(), this.descriptionOfExceptionAdvice());
        return new SqlClientInfoExceptionWrapper(recommendation, sqlClientInfoException);
    }
    public IoExceptionWrapper adviseIoException(IOException ioException) {
        Recommendation recommendation = this.parseIOException(ioException);
        logger.error("Recommended={} for IOException={}, using ExceptionAdvice={}", recommendation.getDescription(), ioException.getMessage(), this.descriptionOfExceptionAdvice());
        return new IoExceptionWrapper(recommendation, ioException);
    }
    public ExceptionWrapper adviseException(Exception exception) {
        Recommendation recommendation = this.parseException(exception);
        logger.error("Recommended={} for Exception={}, using ExceptionAdvice={}", recommendation.getDescription(), exception.getMessage(), this.descriptionOfExceptionAdvice());
        return new ExceptionWrapper(recommendation, exception);
    }
}
