package com.notion.dataSource;

import com.notionds.dataSource.ConnectionSupplier;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotionDs {

	@Test
	public void basicTest() throws SQLException {
		NotionDs.Default notionDs = new NotionDs.Default( new ConnectionSupplier.H2("jdbc:h2:~/test", "", ""));
		Connection connection1 = notionDs.testConnectionBeforeStart();
		Connection connection2 = notionDs.acquireConnection(Duration.ofHours(1));
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
	public void logTest() throws SQLException {
		NotionDs.Default_withLogging notionDs = new NotionDs.Default_withLogging(new ConnectionSupplier.H2("jdbc:h2:~/test", "", ""));
		Connection connection1 = notionDs.testConnectionBeforeStart();
		Connection connection2 = notionDs.acquireConnection(Duration.ofHours(1));
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

		Statement statement = connection1.createStatement();


	}

}
