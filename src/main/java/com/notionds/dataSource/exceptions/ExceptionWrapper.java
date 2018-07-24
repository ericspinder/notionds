package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.accounting.OperationAccounting;

public class ExceptionWrapper extends Exception implements NotionExceptionWrapper {

    private final OperationAccounting operationAccounting;

    public ExceptionWrapper(OperationAccounting operationAccounting, Exception cause) {
        super(operationAccounting.toString(), cause, false, false);
        this.operationAccounting = operationAccounting;
    }

    public OperationAccounting getOperationAccounting() {
        return this.operationAccounting;
    }

}
