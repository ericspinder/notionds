package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.NotionStartupException;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.ExceptionAdvice;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;

public class ConnectionService<O extends Options, NC extends NotionConnectionDelegate, DM extends NotionWrapperManual9<O, NC, EH>, EH extends ExceptionAdvice> {

    private final Class<NC> notionConnectionClass;
    private final Class<DM> delegateMapperClass;
    private final Class<EH> exceptionHandlerClass;
    private final Constructor<NC> notionConnectionConstructor;
    private final Constructor<DM> delegateMapperConstructor;
    private final Constructor<EH> exceptionHandlerConstructor;
    private final O options;


    public ConnectionService(O options) {
        this.options = options;
        this.notionConnectionClass = (Class<NC>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.delegateMapperClass = (Class<DM>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
        this.exceptionHandlerClass = (Class<EH>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
        try {
            try {
                this.notionConnectionConstructor = this.notionConnectionClass.getConstructor(this.options.getClass(), this.delegateMapperClass, Connection.class);
            } catch (ReflectiveOperationException roe) {
                throw new NotionStartupException(NotionStartupException.Type.RefelectiveOperationFailed, this.notionConnectionClass);
            }
            try {
                this.delegateMapperConstructor = this.delegateMapperClass.getConstructor(this.options.getClass(), this.exceptionHandlerClass);
            } catch (ReflectiveOperationException roe) {
                throw new NotionStartupException(NotionStartupException.Type.RefelectiveOperationFailed, this.delegateMapperClass);
            }
            try {
                this.exceptionHandlerConstructor = this.exceptionHandlerClass.getConstructor(this.options.getClass());
            } catch (ReflectiveOperationException roe) {
                throw new NotionStartupException(NotionStartupException.Type.RefelectiveOperationFailed, this.exceptionHandlerClass);
            }
        }
        catch (NullPointerException npe) {
            throw new NotionStartupException(NotionStartupException.Type.NullPointerOnGeneric, ConnectionService.class);
        }
    }

    public NotionConnectionDelegate createNotionConnection(Connection connection) {
        NC notionConnection = null;
        DM delegateMapper = null;
        EH exceptionHandler = null;
        try {
            exceptionHandler = this.exceptionHandlerConstructor.newInstance(this.options);
        }
        catch (ReflectiveOperationException roe) {
            throw new NotionStartupException(NotionStartupException.Type.RefelectiveOperationFailed, this.exceptionHandlerClass);
        }
        try {
            delegateMapper = this.delegateMapperConstructor.newInstance(this.options, exceptionHandler);
        }
        catch (ReflectiveOperationException roe) {
            throw new NotionStartupException(NotionStartupException.Type.RefelectiveOperationFailed, this.delegateMapperClass);
        }
        try {
            notionConnection = this.notionConnectionConstructor.newInstance(this.options, delegateMapper, connection);
        }
        catch (ReflectiveOperationException roe) {
            throw new NotionStartupException(NotionStartupException.Type.RefelectiveOperationFailed, this.notionConnectionClass);
        }
        delegateMapper.setNotionConnectionTree(notionConnection);
        return notionConnection;
    }
}
