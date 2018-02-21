package com.notionds.dataSource;

import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class ExceptionHandler<O extends Options> {


    protected final O options;

    public ExceptionHandler(O options) {
        this.options = options;
    }

    public abstract SQLException handle(SQLException sqlException, DelegatedInstance where);
    public abstract SQLClientInfoException handle(SQLClientInfoException sqlClientInfoException, DelegatedInstance where);
}
