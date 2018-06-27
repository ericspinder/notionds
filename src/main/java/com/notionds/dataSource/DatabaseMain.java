package com.notionds.dataSource;

import com.notionds.dataSource.connection.ConnectionAnalysis;
import com.notionds.dataSource.connection.ConnectionMember_I;
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


    public ConnectionAnalysis.Recommendation remedySQLException(SQLException sqlException, NotionWrapper notionWrapper) {
        ConnectionAnalysis.Recommendation recommendation =  exceptionHandler.handleSQLException(sqlException);
        //analysis
        return recommendation;
    }
    public ConnectionAnalysis.Recommendation remedySQLClientInfoExcpetion(SQLClientInfoException sqlClientInfoException, NotionWrapper notionWrapper) {
        ConnectionAnalysis.Recommendation recommendation =  exceptionHandler.handleSQLClientInfoException(sqlClientInfoException);
        //analysis
        return recommendation;
    }
    public ConnectionAnalysis.Recommendation remedyIoException(IOException ioException, NotionWrapper notionWrapper) {
        ConnectionAnalysis.Recommendation recommendation = exceptionHandler.handleIoException(ioException);
        //analysis
        return recommendation;
    }


}
