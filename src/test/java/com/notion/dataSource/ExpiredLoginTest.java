package com.notion.dataSource;

import com.notionds.dataSource.ConnectionPool;
import com.notionds.dataSource.ConnectionSupplier;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.jdbcProxy.WrapperFactory;
import com.notionds.dataSource.exceptions.Advice;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ExpiredLoginTest {
    @Test
    public void expiredLogin() throws SQLException {
        BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        MutableConnectionSupplier mutableConnectionSupplier = new MutableConnectionSupplier("org.h2.Driver", "jdbc:h2:mem:foo_db", "", "", "SELECT 1 FROM DUAL");
        connectionSuppliers.add(mutableConnectionSupplier);
        connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:mem:foo_db", "", ""));
        ConnectionPool connectionPool = new ConnectionPool(new WrapperFactory(), new Advice.Default(), NotionDs.DEFAULT_OPTIONS_INSTANCE, connectionSuppliers);
        NotionDs notionDs = new NotionDs(connectionPool);

        Connection connection2 = notionDs.getConnection();
        PreparedStatement preparedStatement = connection2.prepareStatement("Select 1 from dual");
        assertInstanceOf(ConnectionArtifact_I.class, preparedStatement);
        ResultSet resultSet1 = preparedStatement.executeQuery();
        assertInstanceOf(ConnectionArtifact_I.class, resultSet1);
        resultSet1.first();
        assertEquals(1, resultSet1.getInt(1));

        //simulate expiration or elimination of username by changing the active connection supplier, then hammer it to get the system to call for a new one
        mutableConnectionSupplier.setPassword("bad password");
        for (int i = 0; i < 50; i++) {
            CompletableFuture.runAsync(() -> {
                try {
                    Connection connection = notionDs.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("Select 2 from dual");
                    ResultSet resultSet = callableStatement.executeQuery();
                    connection.close();
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            });
        }

    }
}
