package com.notion.dataSource;

import com.notionds.dataSource.ConnectionSupplier;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestLogging {

    @Test
    public void logTest() throws SQLException {
        Queue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:~/test", "", ""));
        NotionDs.Default_withLogging notionDs = new NotionDs.Default_withLogging(connectionSuppliers);
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
        assertEquals(1, resultSet1.getInt(1));
        assertEquals(2, resultSet2.getInt(1));
        assertFalse(resultSet1.isClosed());
        resultSet1.close();
        resultSet2.close();
        assertTrue(resultSet1.isClosed());
        assertTrue(resultSet2.isClosed());
        assertFalse(connection1.isClosed());
        assertFalse(connection2.isClosed());
    }

}
