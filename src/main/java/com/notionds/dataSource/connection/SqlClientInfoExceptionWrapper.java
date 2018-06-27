package com.notionds.dataSource.connection;

import com.notionds.dataSource.DatabaseProblem;

import java.sql.SQLClientInfoException;

public class SqlClientInfoExceptionWrapper extends SQLClientInfoException {

    public SqlClientInfoExceptionWrapper(DatabaseProblem databaseProblem, SQLClientInfoException cause) {
        super(databaseProblem.getMessage(), cause);
    }
}
