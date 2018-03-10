package com.notionds.dataSource;

import java.sql.SQLClientInfoException;

public class SqlClientInfoExceptionWrapper extends SQLClientInfoException {

    public SqlClientInfoExceptionWrapper(DatabaseProblem databaseProblem, SQLClientInfoException cause) {
        super(databaseProblem.getMessage(), cause);
    }
}
