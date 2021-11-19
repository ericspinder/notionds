package com.notion.dataSource;

import com.notionds.dataSupplier.jdbc.JdbcSupplier;
import com.notionds.dataSupplier.NotionDs;
import com.notionds.dataSupplier.delegation.Wrapper;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestLogging {

    @Test
    public void statementTest() throws SQLException {
        Queue<NotionDs.Supplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        connectionSuppliers.add(new JdbcSupplier.H2("Server #1", "jdbc:h2:~/test1", "", ""));
        connectionSuppliers.add(new JdbcSupplier.H2("Server #2", "jdbc:h2:~/test2", "", ""));
        NotionDs.Default_withLogging notionDs = new NotionDs.Default_withLogging(connectionSuppliers);
        Connection connection1 = notionDs.testConnection();
        Connection connection2 = notionDs.getConnection(Duration.ofHours(1));
        Statement statement1 = connection1.createStatement();
        Statement statement2 = connection2.createStatement();
        assertTrue(statement1 instanceof Wrapper);
        assertTrue(statement2 instanceof Wrapper);
        statement1.execute("Select 1 from DUAL");
        statement2.execute("select 2 from DUAL");
        ResultSet resultSet1 = statement1.getResultSet();
        ResultSet resultSet2 = statement2.getResultSet();
        assertTrue(resultSet1 instanceof Wrapper);
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
    @Test
    public void preparedStatementTest() throws SQLException {
        Queue<NotionDs.Supplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        connectionSuppliers.add(new JdbcSupplier.H2("Server #1", "jdbc:h2:~/test1", "", ""));
        connectionSuppliers.add(new JdbcSupplier.H2("Server #2", "jdbc:h2:~/test2", "", ""));
        NotionDs.Default_withLogging notionDs = new NotionDs.Default_withLogging(connectionSuppliers);
        Connection connection1 = notionDs.testConnection();
        Connection connection2 = notionDs.getConnection(Duration.ofHours(1));
        PreparedStatement preparedStatement1 = connection1.prepareStatement("SELECT 3 from DUAL");
        PreparedStatement preparedStatement2 = connection2.prepareStatement("SELECT 4 FROM DUAL");
        for (int i = 0; i < 20; i++) {
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            resultSet1.first();
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            resultSet2.first();
            assertEquals(3, resultSet1.getInt(1));
            assertEquals(4, resultSet2.getInt(1));
        }
    }

}
