package com.notionds.dataSource;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class ExceptionAdvice<O extends Options> {


    protected final O options;

    public ExceptionAdvice(O options) {
        this.options = options;
    }

    public abstract Recommendation handleSQLException(SQLException sqlException);
    public abstract Recommendation handleSQLClientInfoException(SQLClientInfoException sqlClientInfoException);
    public abstract Recommendation handleIoException(IOException ioException);

    /**
     * https://en.wikipedia.org/wiki/SQLSTATE
     */
    public enum Recommendation {

        CloseConnectionInstance_Now("Close Connection, Now!"),
        CloseConnectionInstance_When_Finished("Close Connection, when finished"),
        FailoverDatabase_Now("Failover Database, Now!"),
        FailoverDatabase_When_Finished("Failover Database, when finished"),
        NoAction("No additional action")
        ;
        private String description;
        private String sqlState;
        Recommendation(String description) {
            this.description = description;
        }
        public String getDescription() {
            return this.description;
        }
    }
}
