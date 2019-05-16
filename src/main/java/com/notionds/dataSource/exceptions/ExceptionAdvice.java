package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.delegation.proxyV1.log.withLog.DbObjectLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.time.Instant;

public abstract class ExceptionAdvice<O extends Options> {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    public static class DefaultKillConnection<O extends Options> extends ExceptionAdvice<O> {

        private Logger logger = LoggerFactory.getLogger(DefaultKillConnection.class);

        public DefaultKillConnection(O options) {
            super(options);
        }

        @Override
        public Recommendation parseSQLException(SQLException sqlException) {
            logger.error(sqlException.getMessage());
            return Recommendation.CloseConnectionInstance_When_Finished;
        }

        @Override
        public Recommendation parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException) {
            logger.error(sqlClientInfoException.getMessage());
            return Recommendation.CloseConnectionInstance_When_Finished;
        }

        @Override
        public Recommendation parseIOException(IOException ioException) {
            logger.error(ioException.getMessage());
            return Recommendation.CloseConnectionInstance_When_Finished;
        }

        @Override
        public Recommendation parseException(Exception exception) {
            logger.error(exception.getMessage());
            return Recommendation.CloseConnectionInstance_When_Finished;
        }
    }

    protected final O options;

    public ExceptionAdvice(O options) {
        this.options = options;
    }

    protected abstract Recommendation parseSQLException(SQLException sqlException);
    protected abstract Recommendation parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException);
    protected abstract Recommendation parseIOException(IOException ioException);
    protected abstract Recommendation parseException(Exception exception);

    public SqlExceptionWrapper adviseSqlException(SQLException sqlException) {
        return new SqlExceptionWrapper(this.parseSQLException(sqlException), sqlException);
    }
    public SqlClientInfoExceptionWrapper adviseSQLClientInfoException(SQLClientInfoException sqlClientInfoException) {
        return new SqlClientInfoExceptionWrapper(this.parseSQLClientInfoException(sqlClientInfoException), sqlClientInfoException);
    }
    public IoExceptionWrapper adviseIoException(IOException ioException) {
        return new IoExceptionWrapper(this.parseIOException(ioException), ioException);
    }
    public ExceptionWrapper adviseException(Exception exception) {
        return new ExceptionWrapper(this.parseException(exception), exception);
    }

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
