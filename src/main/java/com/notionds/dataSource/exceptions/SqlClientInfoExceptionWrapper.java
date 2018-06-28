package com.notionds.dataSource.exceptions;

import java.sql.SQLClientInfoException;

public class SqlClientInfoExceptionWrapper extends SQLClientInfoException {

    private final ExceptionAccounting exceptionAccounting;

    public SqlClientInfoExceptionWrapper(ExceptionAccounting exceptionAccounting, SQLClientInfoException cause) {
        super(exceptionAccounting.toString(), cause.getFailedProperties(), cause);
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
