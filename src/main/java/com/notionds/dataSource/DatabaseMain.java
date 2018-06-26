package com.notionds.dataSource;

import java.lang.reflect.ParameterizedType;

public abstract class DatabaseMain<O extends Options, EH extends ExceptionHandler> {

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



}
