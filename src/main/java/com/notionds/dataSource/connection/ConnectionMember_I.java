package com.notionds.dataSource.connection;

import com.notionds.dataSource.connection.delegate.DelegateMapper;

import java.util.UUID;

public interface ConnectionMember_I<DM extends DelegateMapper, AW extends AutoCloseable> {

    long getStartTime();
    UUID getConnectionId();
}
