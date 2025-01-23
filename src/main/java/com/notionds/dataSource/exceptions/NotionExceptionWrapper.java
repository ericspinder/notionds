package com.notionds.dataSource.exceptions;


import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

public interface NotionExceptionWrapper {

    String getMessage();
    Recommendation getRecommendation();
    Throwable getCause();

}
