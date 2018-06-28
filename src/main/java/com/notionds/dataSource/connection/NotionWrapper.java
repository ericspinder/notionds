package com.notionds.dataSource.connection;

import com.notionds.dataSource.ExceptionAdvice;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.manual9.*;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.time.Instant;
import java.util.UUID;

public abstract class NotionWrapper<O extends Options, NC extends NotionConnection> {

    private final Class<NC> notionConnectionClass = ((Class<NC>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);

    private NotionConnectionWeakReference<NC> notionConnectionTree = null;
    private final O options;
    private final UUID connectionId = UUID.randomUUID();
    private final VendorConnection vendorConnection;

    public NotionWrapper(O options, VendorConnection vendorConnection) {
        this.options = options;
        this.vendorConnection = vendorConnection;
    }
    public NC getNotionConnection() {
        if (this.notionConnectionTree == null) {
            return this.getNotionConnectionWeakReference().get();
        }
        return this.notionConnectionTree.get();
    }
    public NotionConnectionWeakReference<NC> getNotionConnectionWeakReference() {
        if (this.notionConnectionTree == null) {
            try {
                this.notionConnectionTree = new NotionConnectionWeakReference(notionConnectionClass.getDeclaredConstructor(getClass()).newInstance(this), this);
            }
            catch(Exception e) {
                throw new RuntimeException("Problem creating NotionConnection instance " + e.getMessage());
            }
        }
        return this.notionConnectionTree;
    }

    protected void handleRecommendation(ExceptionAdvice.Recommendation recommendation) {
        //if (ExceptionAdvice.Recommendation.CloseConnectionInstance.equals(recommendation)) {

        //}
    }
    public SQLException handleSQLException(SQLException sqlException, ConnectionMember_I delegatedInstance) {
        Instant exceptionTime = Instant.now();
        ExceptionAccounting exceptionAccounting = new ExceptionAccounting(this.connectionId, this.vendorConnection.getDatabaseMain().remedySQLException(sqlException, this), exceptionTime);
        this.vendorConnection.getConnectionAnalysis().addProblem(exceptionAccounting);
        return new SqlExceptionWrapper(exceptionAccounting, sqlException);
    }
    public SQLClientInfoException handleSQLClientInfoExcpetion(SQLClientInfoException sqlClientInfoException, ConnectionMember_I delegatedInstance) {
        Instant exceptionTime = Instant.now();
        ExceptionAccounting exceptionAccounting = new ExceptionAccounting(this.connectionId, this.vendorConnection.getDatabaseMain().remedySQLClientInfoException(sqlClientInfoException, this), exceptionTime);
        this.vendorConnection.getConnectionAnalysis().addProblem(exceptionAccounting);
        return new SqlClientInfoExceptionWrapper(exceptionAccounting, sqlClientInfoException);
    }
    public IOException handleIoException(IOException ioException, ConnectionMember_I delegatedInstance) {
        Instant exceptionTime = Instant.now();
        ExceptionAccounting exceptionAccounting = new ExceptionAccounting(this.connectionId, this.vendorConnection.getDatabaseMain().remedyIoException(ioException, this), exceptionTime);
        this.vendorConnection.getConnectionAnalysis().addProblem(exceptionAccounting);
        return new IoExceptionWrapper(exceptionAccounting, ioException);
    }
    public VendorConnection getVendorConnection() {
        return this.vendorConnection;
    }
    public Connection getUnderlyingVendorConnection() {
        return this.vendorConnection.getDelegate();
    }
    public final UUID getConnectionId() {
        return this.connectionId;
    }

    public void closeConnectionMember(ConnectionMember_I delegatedInstance) {

    }
    public void closeNotionConnectionTree() {
        //if (options.)
        //this.notionConnectionTree.
    }

    protected abstract void setNotionConnectionTree(NotionConnectionDelegate notionConnectionTree);


}
