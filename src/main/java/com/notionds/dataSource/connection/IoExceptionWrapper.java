package com.notionds.dataSource.connection;

import com.notionds.dataSource.DatabaseProblem;
import com.notionds.dataSource.ExceptionAdvice;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.time.Instant;
import java.util.UUID;

public class IoExceptionWrapper extends IOException {

    private final ExceptionAccounting exceptionAccounting;

    public IoExceptionWrapper(ExceptionAccounting exceptionAccounting, IOException cause) {
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
