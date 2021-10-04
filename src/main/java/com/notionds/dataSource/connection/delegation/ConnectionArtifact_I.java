package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.ConnectionContainer;

public interface ConnectionArtifact_I {

    ConnectionContainer getConnectionMain();

    void closeDelegate() throws Exception;

}
