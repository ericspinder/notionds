package com.notionds.dataSource.exceptions;


public interface NotionExceptionWrapper {

    ExceptionAdvice.Recommendation getRecommendation();
    Throwable getCause();
}
