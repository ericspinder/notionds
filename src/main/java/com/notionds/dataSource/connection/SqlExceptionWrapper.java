package com.notionds.dataSource.connection;

import com.notionds.dataSource.ExceptionAdvice;

import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

public class SqlExceptionWrapper extends SQLException {


    private final ExceptionAccounting exceptionAccounting;

    public SqlExceptionWrapper(ExceptionAccounting exceptionAccounting, SQLException cause) {
        super(exceptionAccounting.toString(), cause);
        this.exceptionAccounting = exceptionAccounting;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public ExceptionAccounting getExceptionAccounting() {
        return this.exceptionAccounting;
    }
}
