package com.notion.dataSource;

import com.notionds.dataSource.ConnectionPool;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.UserNamePasswordConnectionSupplier;
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
    public void statementLoggingTest() throws ExecutionException, InterruptedException {

        BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        connectionSuppliers.add(new UserNamePasswordConnectionSupplier("org.h2.Driver","jdbc:h2:mem:statementTest", "sa", "", "SELECT 5 FROM DUAL"));
        ConnectionPool connectionPool = new ConnectionPool(new LoggingWrapperFactory(new LoggingService("StatementTest",NotionDs.DEFAULT_OPTIONS_INSTANCE)),new Advice.Default(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
        NotionDs notionDs = new NotionDs(connectionPool);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        Executor executor = Executors.newFixedThreadPool(50);
        for (int i = 0; i< 5; i++) {
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
                    wrappedPooledConnection.close();
                }
                catch (SQLException sql ) {
                    System.out.println("caught Statement exception: " + sql.getMessage());
                }
            },executor));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
    }


    @Test
    public void preparedStatementLoggingTest() throws ExecutionException, InterruptedException {
        BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        connectionSuppliers.add(new UserNamePasswordConnectionSupplier("org.h2.Driver","jdbc:h2:mem:preparedStatementTest", "sa", "", "SELECT 6 FROM DUAL"));
        ConnectionPool connectionPool = new ConnectionPool(new LoggingWrapperFactory(new LoggingService("Prepared Statement Test", NotionDs.DEFAULT_OPTIONS_INSTANCE)),new Advice.Default(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
        NotionDs notionDs = new NotionDs(connectionPool);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    Connection connection = notionDs.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT 66 from DUAL");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    assertTrue(resultSet.first());
                    assertEquals(66, resultSet.getInt(1));
                    connection.close();
                }
                catch (Exception e) {
                    System.out.println("caught prepared Statement execute " + e.getMessage());
                }
            }));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
    }
    @Test
    public void callableStatementLoggingTest() throws ExecutionException, InterruptedException {
        BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        connectionSuppliers.add(new UserNamePasswordConnectionSupplier("org.h2.Driver", "jdbc:h2:mem:preparedStatementTest", "sa", "", "SELECT 6 FROM DUAL"));
        ConnectionPool connectionPool = new ConnectionPool(new LoggingWrapperFactory(new LoggingService("Callable Statement Test", NotionDs.DEFAULT_OPTIONS_INSTANCE)), new Advice.Default(), NotionDs.DEFAULT_OPTIONS_INSTANCE, connectionSuppliers);
        NotionDs notionDs = new NotionDs(connectionPool);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            futures.add(CompletableFuture.runAsync(() -> {
                try (Connection connection = notionDs.getConnection()) {
                    CallableStatement callableStatement = connection.prepareCall("select * from (Select 44 from dual) d");
                    assertInstanceOf(ConnectionArtifact_I.class, callableStatement);
                    for (int y = 0; y < 5; y++) {
                        ResultSet resultSet2 = callableStatement.executeQuery();
                        assertInstanceOf(ConnectionArtifact_I.class, resultSet2);
                        resultSet2.first();
                        assertEquals(44, resultSet2.getInt(1));
                    }
                } catch (Exception e) {
                    System.out.println("callable statement exception = " + e.getMessage());
                }
            }));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
    }

}
