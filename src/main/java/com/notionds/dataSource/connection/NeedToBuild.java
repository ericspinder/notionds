package com.notionds.dataSource.connection;

public class NeedToBuild extends Exception {


    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
