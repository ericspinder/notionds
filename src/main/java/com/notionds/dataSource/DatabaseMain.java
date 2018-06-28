package com.notionds.dataSource;

import com.notionds.dataSource.connection.NotionWrapper;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class DatabaseMain<O extends Options, EH extends ExceptionAdvice> {

    private final O options;
    private final Class<EH> exceptionHandlerClass = ((Class<EH>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
    private final EH exceptionHandler;
    private UsernamePassword usernamePassword;
    private String dbUrl;
    private final String friendlyName;

    public DatabaseMain(O options, UsernamePassword usernamePassword, String dbUrl, String friendlyName) {
        this.options = options;
        this.usernamePassword = usernamePassword;
        this.dbUrl = dbUrl;
        this.friendlyName = friendlyName;
        try {
            this.exceptionHandler = this.exceptionHandlerClass.getDeclaredConstructor(options.getClass()).newInstance(options);
        }
        catch (Exception e) {
            throw new RuntimeException("Problem getting delegateTree instance " + e.getMessage());
        }
    }

    /**
     *
     * @param sqlException
     * @param notionWrapper
     * @return Recommendation the initial recommendation for connection at task, may be changed later
     */
    public ExceptionAdvice.Recommendation remedySQLException(SQLException sqlException, NotionWrapper notionWrapper) {
        ExceptionAdvice.Recommendation recommendation =  exceptionHandler.handleSQLException(sqlException);
        //analysis
        return recommendation;
    }

    /**
     *
     * @param sqlClientInfoException
     * @param notionWrapper
     * @return Recommendation the initial recommendation for connection at task, may be changed later
     */
    public ExceptionAdvice.Recommendation remedySQLClientInfoException(SQLClientInfoException sqlClientInfoException, NotionWrapper notionWrapper) {
        ExceptionAdvice.Recommendation recommendation =  exceptionHandler.handleSQLClientInfoException(sqlClientInfoException);
        //analysis
        return recommendation;
    }

    /**
     *
     * @param ioException
     * @param notionWrapper
     * @return Recommendation the initial recommendation for connection at task, may be changed later
     */
    public ExceptionAdvice.Recommendation remedyIoException(IOException ioException, NotionWrapper notionWrapper) {
        ExceptionAdvice.Recommendation recommendation = exceptionHandler.handleIoException(ioException);
        //analysis
        return recommendation;
    }


}
