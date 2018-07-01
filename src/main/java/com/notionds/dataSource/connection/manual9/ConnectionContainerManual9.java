package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.VendorConnection;
import com.notionds.dataSource.connection.generator.ConnectionContainer;

import java.sql.*;

public abstract class ConnectionContainerManual9<O extends Options> extends ConnectionContainer<O, NotionConnectionDelegate> {

    public ConnectionContainerManual9(O options, VendorConnection vendorConnection) {
        super(options, vendorConnection);
    }

    public abstract void close(ConnectionMember_I delegatedInstance);




    public abstract void close() throws SQLException;
}
