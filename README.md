# Notion DataSource

A pooling JDBC datasource wrapper with automatic failover, which tests the connection before using it in all cases; it prevents login failure lockouts by testing each connection before use. 

The API allows new failover connections to be added at anytime, the most common use case would be implementing password changes without restarting the application.

The ConnectionSupplier_I interface also allows custom connection integrations including authentication of user/password managers such as CyberArk (for hiding credentials from the plain text platform configuration), as well as adding additional properties as needed.

It's also designed to 'never starve connections' and if no connection is available from the connectionQueue within the configured time.

The ability to create logging of maskable SQL statements (statements, prepared statements and callable) is a major feature. Note that the default mask is simply 'DUAL' (the default testing SQL statement), to properly use it you will need to override the masking option with a regex appropriate for your use case.

Note that it uses the Java Cleaner API and is only allows Java 9 at the minimum. 


### Main classes

#### com.notionds.datasource.NotionDs - the datasource which takes one parameter, the configured ConnectionPool

#### com.notionds.datasource.ConnectionPool - the connection pool, has 5 parameters...

#### com.notionds.datasource.Advise - the Exception advise has 5 abstract methods, which handles any throwable which may be presented by the underlying JDBC driver. One may implement it for specific handling or use the general default.
        
        protected abstract Recommendation parseSQLException(SQLException sqlException);
        protected abstract Recommendation parseSQLClientInfoException(SQLClientInfoException sqlClientInfoException);
        protected abstract Recommendation parseIOException(IOException ioException);
        protected abstract Recommendation parseException(Exception exception);
        protected abstract Recommendation parseThrowable(Throwable throwable);


#### com.notionds.datasource.ConnectionSupplier_I interface allows for a custom database connection classes to be implemented as needed. Taken as a queue, the first entry is polled at startup and used as the active connection, the rest are kept in order and kept as failover connections. You may also add failover connections ad hoc and all of them are tested, both on entry and upon first use. One may also manually induce a failover by a public method on the ConnectionPool.
       the com.notionds.datasource.ConnectionSupplier class is an immutable implementation which handles typical JDBC database configuration (driver class, username, password, test SQL)

#### com.notionds.dataSource.connection.delegation.WrapperFactory_I wraps all of the database objects into a com.notionds.dataSource.connection.delegation.ConnectionArtifact_I
        com.notionds.dataSource.connection.delegation.jdbcProxy.WrapperFactory is the Java proxy implementation of it, currently used as the defualt
        
        com.notionds.dataSource.connection.delegation.jdbcProxy.logging.LoggingWrapperFactory is the logging version of the same desgin
            This configures logging, note that it is highly recommended that you prevent sensative account information from leaking into the logs by overriding the "com.notionds.logging.replace_regex" Options property.

#### com.notionds.datasource.Options is the mutable property options container. Adding the keys and appropriate objects into the java properties created for it's constructor will enable an override as well as changing the value while running 
        Strings:
    com.notionds.logging.replace_regex - a pattern to replace sensitive sql, note that this is very implementation specific. The default regex is only for testing and simply hides the exact phrase 'DUAL'
    com.notionds.logging.mask - a replacement mask, defualt is "****"

        Integer:
    com.notionds.connection.Max_Queue_Size - Maximum connections held in memory, default is 50 but it's not a hard limit, threading consideration may cause it to be exceeded but not by much.
    com.notionds.connection.min_queue_size - Minimum connections held in memory, also will try to keep the avaiable queue size at this number, until maximum. Default is 5
    com.notionds.datasource.ConnectionPool.timeout_retrieve_connection - Login timeout in milliseconds, default is 2000;
        
        Durations:
    com.notionds.connection_timeout_max_lifetime - Max lifetime of a connection. Default is java.time.Duration.of(2, ChronoUnit.HOURS)),
    
        Booleans:
    com.notionds.logging.enableMask - Enables masking for sensitive parts of SQL statements, note that this is very implementation specific, default is true but only masks 'DUAL' unless a String option is set

Typical usage (from unit test):

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
    } 
    catch (SQLException e) {
        throw new RuntimeException(e);
    }