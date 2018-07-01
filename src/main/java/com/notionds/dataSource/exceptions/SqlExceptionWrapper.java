package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.OperationAccounting;

import java.sql.SQLException;

public class SqlExceptionWrapper extends SQLException {


    private final OperationAccounting operationAccounting;

    public SqlExceptionWrapper(OperationAccounting operationAccounting, SQLException cause) {
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
