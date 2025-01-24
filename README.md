# Notion DataSource

A pooling JDBC datasource wrapper with automatic failover, which tests the connection before using it in all cases. It prevents lockouts by testing connections before use.

### Main classes

#### com.notionds.datasource.NotionDs - the datasource which takes one parameter, the configured ConnectionPool

#### com.notionds.datasource.ConnectionPool - the connection pool, has 5 parameters...

#### com.notionds.datasource.Advise - the Exception advise has 5 abstract methods, which handles any throwable which may be presented by the underlying JDBC driver. One may implement it for specific handling or use the general default.
        
        protected abstract Recommendation parseSQLException(SQLException sqlException);
        protected abstract Recommendation parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException);
        protected abstract Recommendation parseIOException(IOException ioException);
        protected abstract Recommendation parseException(Exception exception);
        protected abstract Recommendation parseThrowable(Throwable throwable);


#### com.notionds.datasource.ConnectionSupplier_I interface allows for a custom database connection classes to be implemented as needed.
       the com.notionds.datasource.ConnectionSupplier class is an immutable implementation which handles typical JDBC database configuration (driver class, username, password, test SQL)

#### com.notionds.dataSource.connection.delegation.WrapperFactory_I wraps all of the database objects into a com.notionds.dataSource.connection.delegation.ConnectionArtifact_I
        com.notionds.dataSource.connection.delegation.jdbcProxy.WrapperFactory is the Java proxy implementation of it, currently used as the defualt
        com.notionds.dataSource.connection.delegation.jdbcProxy.logging.LoggingWrapperFactory is the logging version of the same desgin
            Note that logging is not yet tested in the released Alpha version

#### com.notion.datasource.Options is the mutable property options container, note that they expect exact objects to work. Adding the keys and appropriate objects into the java properties created for it's constructor will enable an override as well as changing the value while running (not yet tested) 
        Integer:
    com.notionds.advice.exception.aggregatorMap.maxSize - The number of exceptions to keep in the logging aggregator default is 1000
    com.notionds.advice.nominal.aggregatorMap.maxSize - The number of nomial logging entries to hold in memory, deault is 1000),
    com.notionds.connection.max_weight_on_create - The maximum amount of time in milliseconds until a RuntimeException is thrown to end", 1000
    com.notionds.connection.Max_Queue_Size - Maximum connections held in memory, default is 50 but it's not a hard limit, threading consideration may cause it to be exceeded but not by much.
    com.notionds.connection.min_queue_size - Minimum connections held in memory, also will try to keep the avaiable queue size at this number, until maximum. Default is 5
    com.notionds.datasource.ConnectionPool.timeout_retrieve_connection - Login timeout in seconds, default is 10);
        
        Duration:
    com.notionds.connections_timeout_in_pool - Amount of time connections will wait in the pool before reaping excess of the number of active in pool connections, Default is java.time.Duration.of(20, ChronoUnit.MINUTES)),
    com.notionds.connections_timeout_in_pool_cool_down - Minimum amount of time between reaping extra active connections, this creates a walk down from the maximum number of connections. Default is java.time.Duration.of(60, ChronoUnit.SECONDS)),
    com.notionds.connection_timeout_on_loan - Default max time before connection is automatically closed, breaking loaned connections. Default is java.time.Duration.of(360, ChronoUnit.MINUTES)),
    com.notionds.connection_timeout_max_lifetime - Max lifetime of a connection. Default is java.time.Duration.of(2, ChronoUnit.HOURS)),

Typical usage (from unit test)

        BlockingQueue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
        connectionSuppliers.add(new ConnectionSupplier.Default("jdbc:h2:mem:foo_db", "", ""));
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