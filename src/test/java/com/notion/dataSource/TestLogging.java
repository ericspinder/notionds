package com.notion.dataSource;

import com.notionds.dataSource.ConnectionPool;
import com.notionds.dataSource.ConnectionSupplier;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.jdbcProxy.logging.LoggingService;
import com.notionds.dataSource.connection.delegation.jdbcProxy.logging.LoggingWrapperFactory;
import com.notionds.dataSource.exceptions.Advice;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestLogging {

    @Test
    public void statementTest() throws SQLException {

        BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:mem:foo_db", "", ""));
        ConnectionPool connectionPool = new ConnectionPool(new LoggingWrapperFactory(new LoggingService(NotionDs.DEFAULT_OPTIONS_INSTANCE)),new Advice.Default_H2(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
        NotionDs notionDs = new NotionDs(connectionPool);
        assertTrue(connectionPool.testAcquireConnection());
        Connection wrappedPooledConnection = notionDs.getConnection();
        Statement statement = wrappedPooledConnection.createStatement();
        assertInstanceOf(ConnectionArtifact_I.class, statement);
        statement.execute("Select 1 from DUAL");
        ResultSet resultSet = statement.getResultSet();
        assertInstanceOf(ConnectionArtifact_I.class, resultSet);
        resultSet.first();
        assertEquals(1, resultSet.getInt(1));
        assertFalse(resultSet.isClosed());
        resultSet.close();
        assertTrue(resultSet.isClosed());
        assertFalse(wrappedPooledConnection.isClosed());
        connectionPool.shutdown();
    }
    @Test
    public void preparedStatementTest() throws SQLException {
        BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:mem:foo_db", "", ""));
        ConnectionPool connectionPool = new ConnectionPool(new LoggingWrapperFactory(new LoggingService(NotionDs.DEFAULT_OPTIONS_INSTANCE)),new Advice.Default_H2(),NotionDs.DEFAULT_OPTIONS_INSTANCE,connectionSuppliers);
        assertTrue(connectionPool.testAcquireConnection());
        NotionDs notionDs = new NotionDs(connectionPool);
        Connection connection = notionDs.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT 2 from DUAL");
        for (int i = 0; i < 20; i++) {
            ResultSet resultSet1 = preparedStatement.executeQuery();
            resultSet1.first();
            assertEquals(2, resultSet1.getInt(1));
        }
        connectionPool.shutdown();
    }

}
