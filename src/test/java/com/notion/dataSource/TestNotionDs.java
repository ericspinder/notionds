package com.notion.dataSource;

import com.notionds.dataSource.ConnectionPool;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.UserNamePasswordConnectionSupplier;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.jdbcProxy.WrapperFactory;
import com.notionds.dataSource.exceptions.Advice;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotionDs {

	@Test
	public void basicTest() {
		BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
		connectionSuppliers.add(new UserNamePasswordConnectionSupplier("org.h2.Driver","jdbc:h2:mem:preparedStatementTest", "sa", "", "SELECT 7 FROM DUAL"));
		ConnectionPool connectionPool = new ConnectionPool(new WrapperFactory(),new Advice.Default(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
		NotionDs notionDs = new NotionDs(connectionPool);
		try (Connection wrappedConnection = notionDs.getConnection()) {
			Statement statement = wrappedConnection.createStatement();
			statement.execute("Select 77 from DUAL");
			ResultSet resultSet = statement.getResultSet();
			resultSet.first();
			assertEquals(77, resultSet.getInt(1));
			assertFalse(resultSet.isClosed());
			resultSet.close();
		} catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}

	@Test
	public void testAsync() {
		BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
		connectionSuppliers.add(new UserNamePasswordConnectionSupplier("org.h2.Driver","jdbc:h2:mem:preparedStatementTest", "sa", "", "SELECT 8 FROM DUAL"));
		ConnectionPool connectionPool = new ConnectionPool(new WrapperFactory(),new Advice.Default(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
		NotionDs notionDs = new NotionDs(connectionPool);
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		Executor executor = Executors.newFixedThreadPool(50);
		for (int i = 0; i< 50; i++) {
			futures.add(CompletableFuture.runAsync(() -> {
                try(Connection wrappedPooledConnection = notionDs.getConnection()) {
                    Statement statement = wrappedPooledConnection.createStatement();
                    assertInstanceOf(ConnectionArtifact_I.class, statement);
                    statement.execute("Select 88 from DUAL");
                    ResultSet resultSet = statement.getResultSet();
                    assertInstanceOf(ConnectionArtifact_I.class, resultSet);
                    resultSet.first();
                    assertEquals(88, resultSet.getInt(1));
                    assertFalse(resultSet.isClosed());
                    resultSet.close();
                    assertTrue(resultSet.isClosed());
                }
                catch (SQLException sql) {
                    System.out.println("caught Statement exception: " + sql.getMessage());
                }
            }, executor));
		}
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}



}
