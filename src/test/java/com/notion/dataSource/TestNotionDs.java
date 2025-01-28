package com.notion.dataSource;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.ConnectionPool;
import com.notionds.dataSource.ConnectionSupplier;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.connection.State;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotionDs {

	@Test
	public void basicTest() throws SQLException {
		BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
		connectionSuppliers.add(new ConnectionSupplier("org.h2.Driver","jdbc:h2:mem:preparedStatementTest", "sa", "", "SELECT 7 FROM DUAL"));
		ConnectionPool connectionPool = new ConnectionPool(new WrapperFactory(),new Advice.Default(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
		NotionDs notionDs = new NotionDs(connectionPool);
		ConnectionContainer connectionContainer = connectionTest(notionDs);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(State.Pooled, connectionContainer.getCurrentState());
	}
	private ConnectionContainer connectionTest(NotionDs notionDs) throws SQLException {
		Connection wrappedConnection = notionDs.getConnection();
		assertInstanceOf(ConnectionArtifact_I.class,wrappedConnection);
		Statement statement = wrappedConnection.createStatement();
		assertInstanceOf(ConnectionArtifact_I.class, statement);
		statement.execute("Select 77 from DUAL");
		ResultSet resultSet = statement.getResultSet();
		assertInstanceOf(ConnectionArtifact_I.class, resultSet);
		resultSet.first();
		assertEquals(77, resultSet.getInt(1));
		assertFalse(resultSet.isClosed());
		resultSet.close();
		assertTrue(resultSet.isClosed());
		assertFalse(wrappedConnection.isClosed());
		wrappedConnection.close();
		return ((ConnectionArtifact_I<?>) wrappedConnection).getConnectionContainer();
	}


	@Test
	public void testAsync() {
		BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
		connectionSuppliers.add(new ConnectionSupplier("org.h2.Driver","jdbc:h2:mem:preparedStatementTest", "sa", "", "SELECT 8 FROM DUAL"));
		ConnectionPool connectionPool = new ConnectionPool(new WrapperFactory(),new Advice.Default(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
		NotionDs notionDs = new NotionDs(connectionPool);
		List<CompletableFuture<Void>> futures = new ArrayList<CompletableFuture<Void>>();
		for (int i = 0; i< 10000; i++) {
			futures.add(CompletableFuture.runAsync(new Runnable() {
				@Override
				public void run() {
					try {
						Connection wrappedPooledConnection = notionDs.getConnection();
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
				}
			}, Executors.newFixedThreadPool(50)));
		}
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[10000]));
	}



}
