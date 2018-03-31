package com.notionds.dataSource;

public abstract class DatabaseConfiguration {

    private UsernamePassword usernamePassword;

    private String dbUrl;

    private String friendlyName;

    public DatabaseConfiguration(UsernamePassword usernamePassword, String dbUrl, String friendlyName) {

    }


}
