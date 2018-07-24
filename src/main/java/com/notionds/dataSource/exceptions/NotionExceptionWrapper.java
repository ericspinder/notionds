package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.accounting.OperationAccounting;

public interface NotionExceptionWrapper {

    OperationAccounting getOperationAccounting();
    Throwable getCause();
}
