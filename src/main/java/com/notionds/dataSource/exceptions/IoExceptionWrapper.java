package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.OperationAccounting;

import java.io.IOException;

public class IoExceptionWrapper extends IOException {

    private final OperationAccounting operationAccounting;

    public IoExceptionWrapper(OperationAccounting operationAccounting, IOException cause) {
        super(operationAccounting.toString(), cause);
        this.operationAccounting = operationAccounting;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

}
