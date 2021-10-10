package com.notionds.dataSource.exceptions;


import com.notionds.dataSource.ConnectionAction;

public interface NotionExceptionWrapper {

    ConnectionAction getRecommendation();
    Throwable getCause();
}
