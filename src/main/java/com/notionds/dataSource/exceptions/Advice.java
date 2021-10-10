package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.ConnectionAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class Advice<O extends Options, S extends NotionDs.ConnectionSupplier_I> {

    private static final Logger logger = LogManager.getLogger(Advice.class);

    public static class Default<S extends NotionDs.ConnectionSupplier_I> extends Advice<Options.Default, S> {

        private static final Logger logger = LogManager.getLogger(Default.class);

        public Default(S connectionSupplier) {
            super(Options.DEFAULT_INSTANCE, connectionSupplier);
        }

        @Override
        protected ConnectionAction parseSQLException(SQLException sqlException) {
            logger.error(sqlException.getMessage());
            return ConnectionAction.CloseCloseable;
        }

        @Override
        protected ConnectionAction parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException) {
            logger.error(sqlClientInfoException.getMessage());
            return ConnectionAction.CloseCloseable;
        }

        @Override
        protected ConnectionAction parseIOException(IOException ioException) {
            logger.error(ioException.getMessage());
            return ConnectionAction.CloseCloseable;
        }

        @Override
        protected ConnectionAction parseException(Exception exception) {
            logger.error(exception.getMessage());
            return ConnectionAction.CloseCloseable;
        }

        @Override
        protected String descriptionOfExceptionAdvice() {
            return "Default Close Connection on all Exceptions";
        }
    }

    protected final O options;
    protected final S connectionSupplier;

    public Advice(O options, S connectionSupplier) {
        this.options = options;
        this.connectionSupplier = connectionSupplier;
    }

    protected abstract String descriptionOfExceptionAdvice();
    protected abstract ConnectionAction parseSQLException(SQLException sqlException);
    protected abstract ConnectionAction parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException);
    protected abstract ConnectionAction parseIOException(IOException ioException);
    protected abstract ConnectionAction parseException(Exception exception);

    public SqlExceptionWrapper adviseSqlException(SQLException sqlException) {
        ConnectionAction connectionAction = this.parseSQLException(sqlException);
        logger.error("Recommended={} for SQLException={}, using ExceptionAdvice={}", connectionAction.getDescription(), sqlException.getMessage(), this.descriptionOfExceptionAdvice());
        return new SqlExceptionWrapper(connectionAction, sqlException);
    }
    public SqlClientInfoExceptionWrapper adviseSQLClientInfoException(SQLClientInfoException sqlClientInfoException) {
        ConnectionAction connectionAction = this.parseSQLClientInfoException(sqlClientInfoException);
        logger.error("Recommended={} for SQLClientInfoException={}, using ExceptionAdvice={}", connectionAction.getDescription(), sqlClientInfoException.getMessage(), this.descriptionOfExceptionAdvice());
        return new SqlClientInfoExceptionWrapper(connectionAction, sqlClientInfoException);
    }
    public IoExceptionWrapper adviseIoException(IOException ioException) {
        ConnectionAction connectionAction = this.parseIOException(ioException);
        logger.error("Recommended={} for IOException={}, using ExceptionAdvice={}", connectionAction.getDescription(), ioException.getMessage(), this.descriptionOfExceptionAdvice());
        return new IoExceptionWrapper(connectionAction, ioException);
    }
    public ExceptionWrapper adviseException(Exception exception) {
        ConnectionAction connectionAction = this.parseException(exception);
        logger.error("Recommended={} for Exception={}, using ExceptionAdvice={}", connectionAction.getDescription(), exception.getMessage(), this.descriptionOfExceptionAdvice());
        return new ExceptionWrapper(connectionAction, exception);
    }
}
