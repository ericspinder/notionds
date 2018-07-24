package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.accounting.OperationAccounting;

import java.sql.SQLClientInfoException;

public class SqlClientInfoExceptionWrapper extends SQLClientInfoException implements NotionExceptionWrapper {

    private final OperationAccounting operationAccounting;

    public SqlClientInfoExceptionWrapper(OperationAccounting operationAccounting, SQLClientInfoException cause) {
        super(operationAccounting.toString(), cause.getFailedProperties(), cause);
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
