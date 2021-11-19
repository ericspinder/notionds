# NotionDs

## JDBC connection pool with advanced instructions, designed for high volume, scalability and stability 
- Add new connections runtime using security of your own, either by extending an Interface or provide a JDBC driver, url, username and password. Then rollover to those connection details you just added while your application runs at full speed. 
- A detailed management interface allows fine grain control and monitoring using Java Management Extensions (JMX) or extended for a more detailed integration into your application. 
- Loaned connections can be set for a timeout, set a sensible default for your application while specifying connections you need longer, untimed or shorter duration. 
- All closeable objects are close() properly when not being kept in pool, BLOBs and CLOBs are free() even when a connection timeout kills the loaned connection. 
- NotionDs is configurable with a logging harness that defaults printing execution time and average. 

---
### Maven Dependency
```
<dependency>
    <groupId>com.notionds</groupId>
    <artifactId>notionds</artifactId>
    <version>1.3.1</version>
</dependency>
```
### H2 database example setup
```
		Queue<NotionDs.ConnectionSupplier_I> connectionSuppliers = new LinkedBlockingDeque<>();
		connectionSuppliers.add(new ConnectionSupplier.H2("jdbc:h2:~/test", "", ""));
		NotionDs.Default notionDs = new NotionDs.Default(connectionSuppliers);

		Connection connection1 = notionDs.getConnection();
		Connection connection2 = notionDs.getConnection(Duration.ofHours(1));
		Statement statement1 = connection1.createStatement();
		Statement statement2 = connection2.createStatement();
		assertTrue(statement1 instanceof ConnectionArtifact_I);
		assertTrue(statement2 instanceof ConnectionArtifact_I);
		statement1.execute("Select 1 from DUAL");
		statement2.execute("select 2 from DUAL");
		ResultSet resultSet1 = statement1.getResultSet();
		ResultSet resultSet2 = statement2.getResultSet();
```
---
### JMX details
