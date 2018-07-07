package com.notionds.dataSource;

import com.notionds.dataSource.connection.generator.ConnectionContainer;
import com.notionds.dataSource.exceptions.ExceptionAdvice;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public abstract class DatabaseMain<O extends Options, EA extends ExceptionAdvice, DA extends DatabaseAnalysis> {

    private final O options;
    @SuppressWarnings("unchecked")
    private final Class<EA> exceptionAdviceClass = ((Class<EA>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
    @SuppressWarnings("unchecked")
    private final Class<DA> databaseAnalysisClass = ((Class<DA>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[2]);
    private final EA exceptionAdvice;
    private final DA databaseAnalysis;
    private UsernamePassword usernamePassword;
    private String dbUrl;
    private final String friendlyName;

    public DatabaseMain(O options, UsernamePassword usernamePassword, String dbUrl, String friendlyName) {
        this.options = options;
        this.usernamePassword = usernamePassword;
        this.dbUrl = dbUrl;
        this.friendlyName = friendlyName;
        try {
            this.exceptionAdvice = this.exceptionAdviceClass.getDeclaredConstructor(options.getClass()).newInstance(options);
        }
        catch (Exception e) {
            throw new RuntimeException("Problem getting delegateTree instance " + e.getMessage());
        }
        try {
            this.databaseAnalysis = this.databaseAnalysisClass.getDeclaredConstructor(options.getClass()).newInstance(options);
        }
        catch (Exception e) {
            throw new RuntimeException("Problem getting delegateTree instance " + e.getMessage());
        }

    }

    /**
     *
     * @param sqlException
     * @param connectionContainer
     * @return Recommendation the initial recommendation for connection at task, may be changed later
     */
    public ExceptionAdvice.Recommendation remedySQLException(SQLException sqlException, ConnectionContainer connectionContainer) {
        ExceptionAdvice.Recommendation recommendation =  exceptionAdvice.adviseSqlException(sqlException);
        //analysis
        return recommendation;
    }

    /**
     *
     * @param sqlClientInfoException
     * @param connectionContainer
     * @return Recommendation the initial recommendation for connection at task, may be changed later
     */
    public ExceptionAdvice.Recommendation remedySQLClientInfoException(SQLClientInfoException sqlClientInfoException, ConnectionContainer connectionContainer) {
        ExceptionAdvice.Recommendation recommendation =  exceptionAdvice.adviseSQLClientInfoException(sqlClientInfoException);
        //analysis
        return recommendation;
    }

    /**
     *
     * @param ioException
     * @param connectionContainer
     * @return Recommendation the initial recommendation for connection at task, may be changed later
     */
    public ExceptionAdvice.Recommendation remedyIoException(IOException ioException, ConnectionContainer connectionContainer) {
        ExceptionAdvice.Recommendation recommendation = exceptionAdvice.adviseIoException(ioException);
        //analysis
        return recommendation;
    }

    public ExceptionAdvice.Recommendation remedyException(Exception exception, ConnectionContainer connectionContainer) {
        ExceptionAdvice.Recommendation recommendation = exceptionAdvice.adviseException(exception);
        //todo check for failover type recommendations
        return recommendation;

    }


}
