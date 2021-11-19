package com.notionds.dataSupplier.exceptions;


public interface NotionExceptionWrapper {

    String getMessage();
    Recommendation getRecommendation();
    Throwable getCause();
}
