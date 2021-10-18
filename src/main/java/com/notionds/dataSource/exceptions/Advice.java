package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.EvictByLowCountMap;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.delegation.jdbcProxy.logging.InvokeAggregator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class Advice<O extends Options, S extends NotionDs.ConnectionSupplier_I, G extends InvokeAggregator> {

    private static final Logger logger = LogManager.getLogger(Advice.class);

    public static class Default<S extends NotionDs.ConnectionSupplier_I> extends Advice<Options.Default, S, InvokeAggregator.Default_intoLog> {

        private static final Logger logger = LogManager.getLogger(Default.class);

        public Default(S connectionSupplier) {
            super(Options.DEFAULT_INSTANCE, connectionSupplier);
        }

        @Override
        protected Recommendation parseSQLException(SQLException sqlException) {
            logger.error(sqlException.getMessage());
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
    protected S connectionSupplier;
    protected final EvictByLowCountMap<String, G> sqlExceptionAggregator;

    public Advice(O options, S connectionSupplier) {
        this.options = options;
        sqlExceptionAggregator = new EvictByLowCountMap<>((Integer) options.get(Options.NotionDefaultIntegers.Advice_Exception_Aggregator_Map_Max_Size.getKey()).getValue());
        this.connectionSupplier = connectionSupplier;
    }

    protected abstract Recommendation parseSQLException(SQLException sqlException);
    protected abstract Recommendation parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException);
    protected abstract Recommendation parseIOException(IOException ioException);
    protected abstract Recommendation parseException(Exception exception);

    public SqlExceptionWrapper adviseSqlException(SQLException sqlException) {
        StringBuilder s = new StringBuilder();
        s.append("NotionDs wrapped SQLException, recommendation=");
        try {
            Recommendation recommendation = this.parseSQLException(sqlException);
            s.append(recommendation);
            return new SqlExceptionWrapper(s.toString(), sqlException, recommendation);
        }
        finally {
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
            return new SqlClientInfoExceptionWrapper(s.toString(), recommendation, sqlClientInfoException);
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
}
