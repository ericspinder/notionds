package com.notionds.dataSource.connection;

import com.notionds.dataSource.connection.delegate.DelegateMapper;

import java.time.Instant;
import java.util.UUID;

public interface ConnectionMember_I extends AutoCloseable {

    Instant getCreateInstant();
    void closeDelegate() throws Exception;
    UUID getConnectionId();
}
