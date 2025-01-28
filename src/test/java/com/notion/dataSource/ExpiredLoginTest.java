package com.notion.dataSource;

import com.notionds.dataSource.ConnectionPool;
import com.notionds.dataSource.ConnectionSupplier;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.jdbcProxy.WrapperFactory;
import com.notionds.dataSource.exceptions.Advice;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ExpiredLoginTest {
    @Test
    public void expiredLogin() throws SQLException, ExecutionException, InterruptedException {
        BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        MutableConnectionSupplier mutableConnectionSupplier = new MutableConnectionSupplier("org.h2.Driver", "jdbc:h2:mem:expired_login", "sa", "", "SELECT 0 FROM DUAL");
        connectionSuppliers.add(mutableConnectionSupplier);
        connectionSuppliers.add(new ConnectionSupplier("org.h2.Driver","jdbc:h2:mem:foo_db", "sa", "", "SELECT 1 FROM DUAL"));
        ConnectionPool connectionPool = new ConnectionPool(new WrapperFactory(), new Advice.Default(), NotionDs.DEFAULT_OPTIONS_INSTANCE, connectionSuppliers);
        NotionDs notionDs = new NotionDs(connectionPool);

        Connection connection2 = notionDs.getConnection();
        PreparedStatement preparedStatement = connection2.prepareStatement("Select 11 from dual");
        assertInstanceOf(ConnectionArtifact_I.class, preparedStatement);
        ResultSet resultSet1 = preparedStatement.executeQuery();
        assertInstanceOf(ConnectionArtifact_I.class, resultSet1);
        resultSet1.first();
        assertEquals(11, resultSet1.getInt(1));

        //simulate expiration or elimination of username by changing the active connection supplier, then hammer it to get the system to call for a new one
        mutableConnectionSupplier.setPassword("bad password");
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    Connection connection = notionDs.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("Select 22 from dual");
                    ResultSet resultSet = callableStatement.executeQuery();
                    connection.close();
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();

    }
    @Test
    public void failedLogin() throws SQLException {
        BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        connectionSuppliers.add(new ConnectionSupplier("org.h2.Driver","jdbc:h2:mem:failedLogin", "badUser", "badPass","SELECT 2 FROM DUAL"));
        connectionSuppliers.add(new ConnectionSupplier("org.h2.Driver","jdbc:h2:mem:failedLogin", "sa", "", "SELECT 3 FROM DUAL"));
        connectionSuppliers.add(new ConnectionSupplier("org.h2.Driver","jdbc:h2:mem:failedLogin", "badUser2", "badPass", "SELECT 4 FROM DUAL"));
        ConnectionPool connectionPool = new ConnectionPool(new WrapperFactory(),new Advice.Default(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
        NotionDs notionDs = new NotionDs(connectionPool);

        Connection connection2 = notionDs.getConnection();
        PreparedStatement preparedStatement = connection2.prepareStatement("Select 33 from dual");
        assertInstanceOf(ConnectionArtifact_I.class, preparedStatement);
        ResultSet resultSet1 = preparedStatement.executeQuery();
        assertInstanceOf(ConnectionArtifact_I.class, resultSet1);
        resultSet1.first();
        assertEquals(33, resultSet1.getInt(1));

        connectionPool.addFailover(new ConnectionSupplier("org.h2.Driver","jdbc:h2:mem:failedLogin", "badUser3", "", "SELECT 1 FROM DUAL"));
        connectionSuppliers.add(new ConnectionSupplier("org.h2.Driver","jdbc:h2:mem:failedLogin", "sa", "", "SELECT 5 FROM DUAL"));

        CallableStatement callableStatement = connection2.prepareCall("select * from (Select 44 from dual) d");
        assertInstanceOf(ConnectionArtifact_I.class, callableStatement);
        ResultSet resultSet2 = callableStatement.executeQuery();
        assertInstanceOf(ConnectionArtifact_I.class, resultSet2);
        resultSet2.first();
        assertEquals(44, resultSet2.getInt(1));
        connection2.close();
    }
}
