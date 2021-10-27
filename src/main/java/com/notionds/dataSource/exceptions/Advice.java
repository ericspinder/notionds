package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class Advice<O extends Options, S extends NotionDs.ConnectionSupplier_I> {

    private static final Logger logger = LogManager.getLogger(Advice.class);

    public static class Default_H2<S extends NotionDs.ConnectionSupplier_I> extends Advice<Options.Default, S> {

        private static final Logger logger = LogManager.getLogger(Default_H2.class);

        public Default_H2() {
            super(Options.DEFAULT_OPTIONS_INSTANCE);
        }

        @Override
        protected Recommendation parseSQLException(SQLException sqlException) {
            logger.error(sqlException.getMessage());
            if (sqlException.getSQLState().equals("28000")) {
                return Recommendation.Authentication_Failover;
            }
            return Recommendation.Close_Closable;
        }

        @Override
        protected Recommendation parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException) {
            logger.error(sqlClientInfoException.getMessage());
            return Recommendation.Close_Closable;
        }

        @Override
        protected Recommendation parseIOException(IOException ioException) {
            logger.error(ioException.getMessage());
            return Recommendation.Close_Closable;
        }

        @Override
        protected Recommendation parseException(Exception exception) {
            logger.error(exception.getMessage());
            return Recommendation.Close_Closable;
        }
    }

    protected final O options;

    public Advice(O options) {
        this.options = options;
    }

    protected abstract Recommendation parseSQLException(SQLException sqlException);
    protected abstract Recommendation parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException);
    protected abstract Recommendation parseIOException(IOException ioException);
    protected abstract Recommendation parseException(Exception exception);

    public SqlExceptionWrapper adviseSqlException(SQLException sqlException, ConnectionArtifact_I connectionArtifact) {
        StringBuilder s = new StringBuilder();
        s.append("NotionDs wrapped SQLException, recommendation=");
        try {
            Recommendation recommendation = this.parseSQLException(sqlException);
            s.append(recommendation);
            return new SqlExceptionWrapper(s.toString(), sqlException, recommendation);
        }
        finally {
            System.out.println("dude");
            if (logger.isDebugEnabled()) {
                s.append('\n').append(sqlException);
                logger.debug(s.toString());
            }
        }
    }
    public SqlClientInfoExceptionWrapper adviseSQLClientInfoException(SQLClientInfoException sqlClientInfoException, ConnectionArtifact_I connectionArtifact) {
        StringBuilder s = new StringBuilder();
        s.append("NotionDs wrapped SQLClientInfoException, recommendation=");
        try {
            Recommendation recommendation = this.parseSQLClientInfoException(sqlClientInfoException);
            s.append(recommendation);
            return new SqlClientInfoExceptionWrapper(s.toString(), recommendation, sqlClientInfoException);
        }
        finally {
            if (logger.isDebugEnabled()) {
                s.append('\n').append(sqlClientInfoException);
                logger.debug(s.toString());
            }
        }
    }
    public IoExceptionWrapper adviseIoException(IOException ioException, ConnectionArtifact_I connectionArtifact) {
        StringBuilder s = new StringBuilder();
        s.append("NotionDs wrapped IOException, recommendation=");
        try {
            Recommendation recommendation = this.parseIOException(ioException);
            s.append(recommendation);
            return new IoExceptionWrapper(s.toString(), recommendation, ioException);
        }
        finally {
            if (logger.isDebugEnabled()) {
                s.append('\n').append(ioException);
                logger.debug(s.toString());
            }
        }
    }
    public ExceptionWrapper adviseException(Exception exception, ConnectionArtifact_I connectionArtifact) {
        StringBuilder s = new StringBuilder();
        s.append("NotionDs wrapped Exception, recommendation=");
        try {
            Recommendation recommendation = this.parseException(exception);
            s.append(recommendation);
            return new ExceptionWrapper(s.toString(), recommendation, exception);
        }
        finally {
            if (logger.isDebugEnabled()) {
                s.append('\n').append(exception);
                logger.debug(s.toString());
            }
        }
    }
}
