package com.notionds.dataSource.connection.cleanup;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.NeedToBuild;

import java.sql.Connection;

public abstract class ConnectionCleanup<O extends Options, NC extends NotionCleanup> {

    protected final O options;
    protected final NC notionCleanup;

    public ConnectionCleanup(O options, NC notionCleanup) {
        this.options = options;
        this.notionCleanup = notionCleanup;
    }

    public abstract Connection getConnection(ConnectionContainer connectionContainer);

    public abstract void closeAll();

    public abstract void close(ConnectionMember_I connectionMember);

    public abstract ConnectionMember_I add(ConnectionMember_I connectionMember, Object delegate, ConnectionMember_I parent);

}
