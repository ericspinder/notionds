package com.notionds.dataSource.connection;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.DelegatedInstance;

import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class ExceptionHandler<O extends Options> {


    protected final O options;

    public ExceptionHandler(O options) {
        this.options = options;
    }

    public abstract SQLException handle(SQLException sqlException);
    public abstract SQLClientInfoException handle(SQLClientInfoException sqlClientInfoException);
}
