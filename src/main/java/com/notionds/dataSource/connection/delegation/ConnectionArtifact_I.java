package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.connection.Container;

import java.util.UUID;

public interface ConnectionArtifact_I {

    UUID getArtifactId();

    Container getContainer();

    Object getDelegate();

}
