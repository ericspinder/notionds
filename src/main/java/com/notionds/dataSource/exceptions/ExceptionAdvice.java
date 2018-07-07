package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class ExceptionAdvice<O extends Options> {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    public static class DefaultKillConnection<O extends Options> extends ExceptionAdvice<O> {

        private Logger logger = LoggerFactory.getLogger(DefaultKillConnection.class);

        public DefaultKillConnection(O options) {
            super(options);
        }

        @Override
        public Recommendation adviseSqlException(SQLException sqlException) {
            logger.error(sqlException.getMessage());
            return Recommendation.CloseConnectionInstance_When_Finished;
        }

        @Override
        public Recommendation adviseSQLClientInfoException(SQLClientInfoException sqlClientInfoException) {
            logger.error(sqlClientInfoException.getMessage());
            return Recommendation.CloseConnectionInstance_When_Finished;
        }

        @Override
        public Recommendation adviseIoException(IOException ioException) {
            logger.error(ioException.getMessage());
            return Recommendation.CloseConnectionInstance_When_Finished;
        }

        @Override
        public Recommendation adviseException(Exception exception) {
            logger.error(exception.getMessage());
            return Recommendation.CloseConnectionInstance_When_Finished;
        }
    }

    protected final O options;

    public ExceptionAdvice(O options) {
        this.options = options;
    }

    public abstract Recommendation adviseSqlException(SQLException sqlException);
    public abstract Recommendation adviseSQLClientInfoException(SQLClientInfoException sqlClientInfoException);
    public abstract Recommendation adviseIoException(IOException ioException);
    public abstract Recommendation adviseException(Exception exception);

    /**
     * https://en.wikipedia.org/wiki/SQLSTATE
     */
    public enum Recommendation {

        CloseConnectionInstance_Now("Close Connection, Now!"),
        CloseConnectionInstance_When_Finished("Close Connection, when finished"),
        FailoverDatabase_Now("Failover Database, Now!"),
        FailoverDatabase_When_Finished("Failover Database, when finished"),
        NoAction("No additional action")
        ;
        private String description;
        private String sqlState;
        Recommendation(String description) {
            this.description = description;
        }
        public String getDescription() {
            return this.description;
        }
    }
}
