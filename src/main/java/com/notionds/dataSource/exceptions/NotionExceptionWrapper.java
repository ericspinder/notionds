package com.notionds.dataSource.exceptions;


public interface NotionExceptionWrapper {

    String getMessage();
    Recommendation getRecommendation();
    Throwable getCause();
}
