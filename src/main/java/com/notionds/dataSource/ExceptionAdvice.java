package com.notionds.dataSource;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionAnalysis;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class ExceptionAdvice<O extends Options> {


    protected final O options;

    public ExceptionAdvice(O options) {
        this.options = options;
    }

    public abstract ConnectionAnalysis.Recommendation handleSQLException(SQLException sqlException);
    public abstract ConnectionAnalysis.Recommendation handleSQLClientInfoException(SQLClientInfoException sqlClientInfoException);
    public abstract ConnectionAnalysis.Recommendation handleIoException(IOException ioException);
}
