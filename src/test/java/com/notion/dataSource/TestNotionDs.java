package com.notion.dataSource;

import com.notionds.dataSource.ConnectionSupplier;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.NotionStartupException;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotionDs {

	@Test
	public void basicTest() throws SQLException {
		Queue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
		connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:~/test", "", ""));
		NotionDs.Default notionDs = new NotionDs.Default(connectionSuppliers);

		Connection connection1 = notionDs.testConnection();
		Connection connection2 = notionDs.getConnection(Duration.ofHours(1));
		Statement statement1 = connection1.createStatement();
		Statement statement2 = connection2.createStatement();
		assertTrue(statement1 instanceof ConnectionArtifact_I);
		assertTrue(statement2 instanceof ConnectionArtifact_I);
		statement1.execute("Select 1 from DUAL");
		statement2.execute("select 2 from DUAL");
		ResultSet resultSet1 = statement1.getResultSet();
		ResultSet resultSet2 = statement2.getResultSet();
		assertTrue(resultSet1 instanceof ConnectionArtifact_I);
		resultSet1.first();
		resultSet2.first();
		assertTrue(resultSet1.getInt(1) == 1);
		assertTrue(resultSet2.getInt(1) == 2);
		assertFalse(resultSet1.isClosed());
		resultSet1.close();
		resultSet2.close();
		assertTrue(resultSet1.isClosed());
		assertTrue(resultSet2.isClosed());
		assertFalse(connection1.isClosed());
		assertFalse(connection2.isClosed());
		connection1.close();
		connection2.close();
		assertFalse(connection1.isClosed());
		assertFalse(connection2.isClosed());
	}
	@Test
	public void failedLogin() throws SQLException {
		Queue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
		connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:~/test", "badUser", "badPass"));
		connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:~/test", "", ""));
		NotionDs.Default notionDs = new NotionDs.Default(connectionSuppliers);
		NotionStartupException notionStartupException = assertThrows(NotionStartupException.class, () ->notionDs.testConnection());

		Connection connection2 = notionDs.getConnection(Duration.ofHours(1));
		PreparedStatement preparedStatement = connection2.prepareStatement("Select 1 from dual");
		assertInstanceOf(ConnectionArtifact_I.class, preparedStatement);
		ResultSet resultSet1 = preparedStatement.executeQuery();
		assertInstanceOf(ConnectionArtifact_I.class, resultSet1);
		resultSet1.first();
		assertEquals(1, resultSet1.getInt(1));

		CallableStatement callableStatement = connection2.prepareCall("Select 2 from dual");
		assertInstanceOf(ConnectionArtifact_I.class, callableStatement);
		ResultSet resultSet2 = callableStatement.executeQuery();
		assertInstanceOf(ConnectionArtifact_I.class, resultSet2);
		resultSet2.first();
		assertEquals(2, resultSet2.getInt(1));
		//connection2.close();
		//Connection connection2 = notionDs.testConnectionBeforeStart();
	}

}
