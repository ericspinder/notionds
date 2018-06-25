package com.notionds.dataSource.connection.delegate;

import com.notionds.dataSource.Options;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class ExceptionHandler<O extends Options> {


    protected final O options;

    public ExceptionHandler(O options) {
        this.options = options;
    }

    public enum Recommendation {
        CloseConnectionInstance("Close Connection"),
        FailoverDatabase("Failover Database"),
        NoAction("No additional action")
        ;
        private String description;
        Recommendation(String description) {
            this.description = description;
        }
        public String getDescription() {
            return this.description;
        }
    }

    public abstract Recommendation handleSQLException(SQLException sqlException);
    public abstract Recommendation handleSQLClientInfoException(SQLClientInfoException sqlClientInfoException);
    public abstract Recommendation handleIoException(IOException ioException);
}
