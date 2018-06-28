package com.notionds.dataSource.connection;

import com.notionds.dataSource.ExceptionAdvice;

import java.sql.ClientInfoStatus;
import java.sql.SQLClientInfoException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

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
