package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.ConnectionContainer;

import java.util.UUID;

public interface ConnectionArtifact_I {

    UUID getUuid();

    ConnectionContainer getConnectionMain();

    void closeDelegate();

}
