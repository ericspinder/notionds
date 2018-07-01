package com.notionds.dataSource.connection;

import com.notionds.dataSource.connection.manual9.NotionConnection_Keep_for_now;
import com.notionds.dataSource.connection.generator.ConnectionContainer;

public class NotionConnectionWeakReference<NW extends NotionConnection_Keep_for_now, Connection> extends NotionWeakReference<NW, Connection> {

    public NotionConnectionWeakReference(NW notionConnection, Connection connection, ConnectionContainer connectionContainer) {
        super(notionConnection,connection, connectionContainer);
    }

}
