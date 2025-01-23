package com.notion.dataSource;

import com.notionds.dataSource.ConnectionPool;
import com.notionds.dataSource.ConnectionSupplier;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.NotionStartupException;
import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.jdbcProxy.WrapperFactory;
import com.notionds.dataSource.connection.delegation.jdbcProxy.logging.LoggingService;
import com.notionds.dataSource.connection.delegation.jdbcProxy.logging.LoggingWrapperFactory;
import com.notionds.dataSource.exceptions.Advice;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.Duration;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotionDs {

	@Test
	public void basicTest() throws SQLException {
		BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
		connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:mem:foo_db", "", ""));
		ConnectionPool connectionPool = new ConnectionPool(new WrapperFactory(),new Advice.Default_H2(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
		NotionDs notionDs = new NotionDs(connectionPool);
		Connection wrappedConnection = notionDs.getConnection();
		assertInstanceOf(ConnectionArtifact_I.class,wrappedConnection);
		Statement statement = wrappedConnection.createStatement();
		assertInstanceOf(ConnectionArtifact_I.class, statement);
		statement.execute("Select 1 from DUAL");
		ResultSet resultSet = statement.getResultSet();
		assertInstanceOf(ConnectionArtifact_I.class, resultSet);
		resultSet.first();
        assertEquals(1, resultSet.getInt(1));
		assertFalse(resultSet.isClosed());
		resultSet.close();
		assertTrue(resultSet.isClosed());
		assertFalse(wrappedConnection.isClosed());
		wrappedConnection.close();
		assertFalse(wrappedConnection.isClosed());
        assertEquals(((ConnectionArtifact_I<Connection>) wrappedConnection).getConnectionContainer().getCurrentState(), State.Pooled);
	}
	@Test
	public void failedLogin() throws SQLException {
		BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
		connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:mem:foo_db", "badUser", "badPass"));
		connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:mem:foo_db", "", ""));
		connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:mem:foo_db", "badUser2", "badPass"));
		ConnectionPool connectionPool = new ConnectionPool(new WrapperFactory(),new Advice.Default_H2(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
		NotionDs notionDs = new NotionDs(connectionPool);

		Connection connection2 = notionDs.getConnection();
		PreparedStatement preparedStatement = connection2.prepareStatement("Select 1 from dual");
		assertInstanceOf(ConnectionArtifact_I.class, preparedStatement);
		ResultSet resultSet1 = preparedStatement.executeQuery();
		assertInstanceOf(ConnectionArtifact_I.class, resultSet1);
		resultSet1.first();
		assertEquals(1, resultSet1.getInt(1));

		connectionPool.addFailover(new ConnectionSupplier.H2("jdbc:h2:mem:foo_db", "badUser3", ""));
		connectionPool.addFailover(new ConnectionSupplier.H2("jdbc:h2:mem:foo_db", "", ""));

		CallableStatement callableStatement = connection2.prepareCall("select * from (Select 2 from dual) d");
		assertInstanceOf(ConnectionArtifact_I.class, callableStatement);
		ResultSet resultSet2 = callableStatement.executeQuery();
		assertInstanceOf(ConnectionArtifact_I.class, resultSet2);
		resultSet2.first();
		assertEquals(2, resultSet2.getInt(1));
		connection2.close();
	}
	@Test
	public void expiredPasswordLogin() throws SQLException {
		BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
		MutableConnectionSupplier mutableConnectionSupplier = new MutableConnectionSupplier("org.h2.Driver","jdbc:h2:mem:foo_db", "", "","SELECT 1 FROM DUAL");
		connectionSuppliers.add(mutableConnectionSupplier);
		connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:mem:foo_db", "", ""));
		ConnectionPool connectionPool = new ConnectionPool(new WrapperFactory(),new Advice.Default_H2(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
		NotionDs notionDs = new NotionDs(connectionPool);

		Connection connection2 = notionDs.getConnection();
		PreparedStatement preparedStatement = connection2.prepareStatement("Select 1 from dual");
		assertInstanceOf(ConnectionArtifact_I.class, preparedStatement);
		ResultSet resultSet1 = preparedStatement.executeQuery();
		assertInstanceOf(ConnectionArtifact_I.class, resultSet1);
		resultSet1.first();
		assertEquals(1, resultSet1.getInt(1));

		//simulate expiration of password by changing it
		mutableConnectionSupplier.setPassword("bad Password");

		CallableStatement callableStatement = connection2.prepareCall("Select 2 from dual");
		assertInstanceOf(ConnectionArtifact_I.class, callableStatement);
		ResultSet resultSet2 = callableStatement.executeQuery();
		assertInstanceOf(ConnectionArtifact_I.class, resultSet2);
		resultSet2.first();
		assertEquals(2, resultSet2.getInt(1));
		connection2.close();
	}
}
