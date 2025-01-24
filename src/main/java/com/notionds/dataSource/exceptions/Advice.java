package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class Advice {

    private static final Logger logger = LogManager.getLogger(Advice.class);

    public static class Default extends Advice {

        private static final Logger logger = LogManager.getLogger(Default.class);

        public Default() {
            super(NotionDs.DEFAULT_OPTIONS_INSTANCE);
        }

        @Override
        protected Recommendation parseSQLException(SQLException sqlException) {
            if (sqlException.getSQLState().equals("28000")) {
                return Recommendation.Authentication_Failover;
            }
            return Recommendation.Close_Closable;
        }

        @Override
        protected Recommendation parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException) {
            return Recommendation.Close_Closable;
        }

        @Override
        protected Recommendation parseIOException(IOException ioException) {
            return Recommendation.Close_Closable;
        }

        @Override
        protected Recommendation parseException(Exception exception) {
            return Recommendation.Close_Closable;
        }

        @Override
        protected Recommendation parseThrowable(Throwable throwable) {
            logger.error(throwable.getMessage());
            return Recommendation.Close_Closable;
        }
    }

    protected final Options options;

    public Advice(Options options) {
        this.options = options;
    }

    protected abstract Recommendation parseSQLException(SQLException sqlException);
    protected abstract Recommendation parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException);
    protected abstract Recommendation parseIOException(IOException ioException);
    protected abstract Recommendation parseException(Exception exception);
    protected abstract Recommendation parseThrowable(Throwable throwable);

    public SqlExceptionWrapper adviseSqlException(SQLException sqlException) {
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
    public SqlClientInfoExceptionWrapper adviseSQLClientInfoException(SQLClientInfoException sqlClientInfoException) {
        StringBuilder s = new StringBuilder();
        s.append("NotionDs wrapped SQLClientInfoException, recommendation=");
        try {
            Recommendation recommendation = this.parseSQLClientInfoException(sqlClientInfoException);
            s.append(recommendation);
            return new SqlClientInfoExceptionWrapper(s.toString(), recommendation,sqlClientInfoException);
        }
        finally {
            if (logger.isDebugEnabled()) {
                s.append('\n').append(sqlClientInfoException);
                logger.debug(s.toString());
            }
        }
    }
    public IoExceptionWrapper adviseIoException(IOException ioException) {
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
    public ExceptionWrapper adviseException(Exception exception) {
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
    public ThrowableWrapper adviseThrowable(Throwable throwable) {
        StringBuilder s = new StringBuilder();
        s.append("NotionDs wrapped Exception, recommendation=");
        try {
            Recommendation recommendation = this.parseThrowable(throwable);
            s.append(recommendation);
            return new ThrowableWrapper(s.toString(), recommendation, throwable);
        }
        finally {
            if (logger.isDebugEnabled()) {
                s.append('\n').append(throwable);
                logger.debug(s.toString());
            }
        }
    }
    public static void PrintCause(Throwable t, StringBuilder stringBuilder) {
        stringBuilder.append(t.getMessage());
        if (t.getCause() != null) {
            stringBuilder.append("\n caused by: ");
            PrintCause(t.getCause(), stringBuilder);
        }
    }
}
