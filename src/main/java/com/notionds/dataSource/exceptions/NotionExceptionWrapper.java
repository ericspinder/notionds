package com.notionds.dataSource.exceptions;


import com.notionds.dataSource.Recommendation;

public interface NotionExceptionWrapper {

    Recommendation getRecommendation();
    Throwable getCause();
}
