package com.notion.dataSource;

import com.notionds.dataSource.ConnectionSupplier;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import org.junit.jupiter.api.Test;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotionDs {

	@Test
	public void test() throws SQLException {
		NotionDs.Default notionDs = new NotionDs.Default( new ConnectionSupplier.H2("jdbc:h2:~/test", "", ""));
		Connection connection = notionDs.testConnectionBeforeStart();
		Statement statement = connection.createStatement();
		assertTrue(statement instanceof ConnectionArtifact_I);
		statement.execute("Select 1 from DUAL");
		ResultSet resultSet = statement.getResultSet();
		assertTrue(resultSet instanceof ConnectionArtifact_I);
		resultSet.first();
		int i = resultSet.getInt(1);
		assertTrue(i == 1);
		assertFalse(resultSet.isClosed());
		resultSet.close();
		assertTrue(resultSet.isClosed());
		assertFalse(connection.isClosed());


	}

}
