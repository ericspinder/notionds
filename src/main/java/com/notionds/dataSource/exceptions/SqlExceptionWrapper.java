package com.notionds.dataSource.exceptions;

import java.sql.SQLException;

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
