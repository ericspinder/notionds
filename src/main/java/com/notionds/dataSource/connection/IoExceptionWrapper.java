package com.notionds.dataSource.connection;

import com.notionds.dataSource.DatabaseProblem;

import java.io.IOException;

public class IoExceptionWrapper extends IOException {

    public IoExceptionWrapper(DatabaseProblem databaseProblem, IOException cause) {
        super(databaseProblem.getMessage(), cause);
    }
    @Override
    public IOException fillInStackTrace() {
        return this;
    }

}
