package com.notion.dataSource;

import com.notionds.dataSource.ConnectionPool;
import com.notionds.dataSource.ConnectionSupplier;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.jdbcProxy.logging.LoggingService;
import com.notionds.dataSource.connection.delegation.jdbcProxy.logging.LoggingWrapperFactory;
import com.notionds.dataSource.exceptions.Advice;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestLogging {

    @Test
    public void statementTest() throws SQLException, ExecutionException, InterruptedException {

        BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        connectionSuppliers.add(new ConnectionSupplier("org.h2.Driver","jdbc:h2:mem:statementTest", "sa", "", "SELECT 5 FROM DUAL"));
        ConnectionPool connectionPool = new ConnectionPool(new LoggingWrapperFactory(new LoggingService("StatementTest",NotionDs.DEFAULT_OPTIONS_INSTANCE)),new Advice.Default(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
        NotionDs notionDs = new NotionDs(connectionPool);
        List<CompletableFuture<Void>> futures = new ArrayList<CompletableFuture<Void>>();
        for (int i = 0; i< 50; i++) {
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    Connection wrappedPooledConnection = notionDs.getConnection();
                    Statement statement = wrappedPooledConnection.createStatement();
                    assertInstanceOf(ConnectionArtifact_I.class, statement);
                    statement.execute("Select 55 from DUAL");
                    ResultSet resultSet = statement.getResultSet();
                    assertInstanceOf(ConnectionArtifact_I.class, resultSet);
                    resultSet.first();
                    assertEquals(55, resultSet.getInt(1));
                    assertFalse(resultSet.isClosed());
                }
                catch (SQLException sql) {
                    System.out.println("caught Statement exception: " + sql.getMessage());
                }
            }, Executors.newFixedThreadPool(50)));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
    }

    @Test
    public void preparedStatementTest() throws SQLException, ExecutionException, InterruptedException {
        BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        connectionSuppliers.add(new ConnectionSupplier("org.h2.Driver","jdbc:h2:mem:preparedStatementTest", "sa", "", "SELECT 6 FROM DUAL"));
        ConnectionPool connectionPool = new ConnectionPool(new LoggingWrapperFactory(new LoggingService("Prepared Statement Test", NotionDs.DEFAULT_OPTIONS_INSTANCE)),new Advice.Default(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
        NotionDs notionDs = new NotionDs(connectionPool);
        Connection connection = notionDs.getConnection();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT 66 from DUAL");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    assertTrue(resultSet.first());
                    assertEquals(66, resultSet.getInt(1));
                }
                catch (Exception e) {
                    System.out.println("caught prepared Statement execute " + e.getMessage());
                }
            }));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
    }

}
