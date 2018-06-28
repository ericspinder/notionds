package com.notionds.dataSource.exceptions;

public class ExceptionWrapper extends Exception {

    private final ExceptionAccounting exceptionAccounting;

    public ExceptionWrapper(ExceptionAccounting exceptionAccounting, Exception cause) {
        super(exceptionAccounting.toString(), cause, false, false);
        this.exceptionAccounting = exceptionAccounting;
    }

    public ExceptionAccounting getExceptionAccounting() {
        return this.exceptionAccounting;
    }

}
