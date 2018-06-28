package com.notionds.dataSource.exceptions;

import java.io.IOException;

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
