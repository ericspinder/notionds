package com.notionds.dataSource.connection.delegate;

import com.notionds.dataSource.Options;

import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class ExceptionHandler<O extends Options> {


    protected final O options;

    public ExceptionHandler(O options) {
        this.options = options;
    }

    public abstract SQLException handle(SQLException sqlException);
    public abstract Exception handle(SQLClientInfoException sqlClientInfoException);
}
