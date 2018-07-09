package com.notionds.dataSource.connection;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.cleanup.ConnectionCleanup;
import com.notionds.dataSource.connection.delegation.DelegationOfNotion;
import com.notionds.dataSource.connection.cleanup.NotionCleanup;
import com.notionds.dataSource.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.util.UUID;

public class ConnectionContainer<O extends Options, D extends DelegationOfNotion, CC extends ConnectionCleanup<O, NC>, NC extends NotionCleanup<O, CC, VC>, VC extends VendorConnection> {

    private final Logger logger = LoggerFactory.getLogger(ConnectionContainer.class);


    private final O options;
    private final UUID connectionId = UUID.randomUUID();
    private final VC vendorConnection;
    private final D delegation;
    private final CC connectionCleanup;

    public ConnectionContainer(O options, VC vendorConnection, D delegation, NC notionCleanup) {
        this.options = options;
        this.vendorConnection = vendorConnection;
        this.delegation = delegation; //((Class<W>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor().newInstance(options);
        this.connectionCleanup = notionCleanup.register(vendorConnection);
    }
    public Connection getNotionConnection() {
        return this.connectionCleanup.getConnection();
    }

    protected void handleRecommendation(ExceptionAdvice.Recommendation recommendation) {
        //if (ExceptionAdvice.Recommendation.CloseConnectionInstance.equals(recommendation)) {

        //}
    }
    public SQLException handleSQLException(SQLException sqlException, ConnectionMember_I delegatedInstance) {
        OperationAccounting operationAccounting = delegatedInstance.getOperationAccounting()
                .setFinishTime(Instant.now())
                .setRecommendation(this.vendorConnection.getDatabaseMain().remedySQLException(sqlException, this));
        this.vendorConnection.getConnectionAnalysis().addException(operationAccounting, delegatedInstance);
        return new SqlExceptionWrapper(operationAccounting, sqlException);
    }
    public SQLClientInfoException handleSQLClientInfoExcpetion(SQLClientInfoException sqlClientInfoException, ConnectionMember_I delegatedInstance) {
        OperationAccounting operationAccounting = delegatedInstance.getOperationAccounting()
                .setFinishTime(Instant.now())
                .setRecommendation(this.vendorConnection.getDatabaseMain().remedySQLClientInfoException(sqlClientInfoException, this));
        this.vendorConnection.getConnectionAnalysis().addException(operationAccounting, delegatedInstance);
        return new SqlClientInfoExceptionWrapper(operationAccounting, sqlClientInfoException);
    }
    public IOException handleIoException(IOException ioException, ConnectionMember_I delegatedInstance) {
        OperationAccounting operationAccounting = delegatedInstance.getOperationAccounting()
                .setFinishTime(Instant.now())
                .setRecommendation(this.vendorConnection.getDatabaseMain().remedyIoException(ioException, this));
        this.vendorConnection.getConnectionAnalysis().addException(operationAccounting, delegatedInstance);
        return new IoExceptionWrapper(operationAccounting, ioException);
    }
    public Exception handleException(Exception exception, ConnectionMember_I delegatedInstance) {
        OperationAccounting operationAccounting = delegatedInstance.getOperationAccounting()
                .setFinishTime(Instant.now())
                .setRecommendation(this.vendorConnection.getDatabaseMain().remedyException(exception, this));
        this.vendorConnection.getConnectionAnalysis().addException(operationAccounting, delegatedInstance);
        return new ExceptionWrapper(operationAccounting, exception);
    }

    public final UUID getConnectionId() {
        return this.connectionId;
    }

    public void closeSqlException(ConnectionMember_I delegatedInstance) throws SQLException {

    }
    public void closeIoException(ConnectionMember_I delegatedInstance) throws IOException {

    }
    public void closeFree(ConnectionMember_I delgatedInstance) throws SQLException {

    }
    public void closeNotNeeded(ConnectionMember_I delegatedInstance) {

    }


    public ConnectionMember_I wrap(Object delegate, Class clazz, ConnectionMember_I parent) {
        ConnectionMember_I wrapped = delegation.getDelegate(this, delegate, clazz.getClass());
        this.connectionCleanup.add(wrapped, delegate, parent);
        return wrapped;
    }
}
