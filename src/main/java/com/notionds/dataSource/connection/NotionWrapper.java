package com.notionds.dataSource.connection;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.manual9.*;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.UUID;

public abstract class NotionWrapper<O extends Options, NC extends NotionConnection> {

    private final Class<NC> notionConnectionClass = ((Class<NC>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);

    private ConnectionMemberWeakReference<NC> notionConnectionTree = null;
    private final O options;
    private final UUID connectionId = UUID.randomUUID();
    private final VendorConnection vendorConnection;

    public NotionWrapper(O options, VendorConnection vendorConnection) {
        this.options = options;
        this.vendorConnection = vendorConnection;
    }
    public NC getNotionConnection() {
        if (this.notionConnectionTree == null) {
            try {
                this.notionConnectionTree = new ConnectionMemberWeakReference<>(notionConnectionClass.getDeclaredConstructor(getClass()).newInstance(this), this, null);
            }
            catch(Exception e) {
                throw new RuntimeException("Problem creating NotionConnection instance " + e.getMessage());
            }
        }
        return this.notionConnectionTree.get();
    }

    protected void handleRecomendation(ConnectionAnalysis.Recommendation recommendation) {
        if (ConnectionAnalysis.Recommendation.CloseConnectionInstance.equals(recommendation)) {

        }
    }

    public SQLException handleSQLException(SQLException sqlException, ConnectionMember_I delegatedInstance) {
        ConnectionAnalysis.Recommendation recommendation =  this.vendorConnection.getDatabaseMain().remedySQLException(sqlException);

    }
    public SQLClientInfoException handleSQLClientInfoExcpetion(SQLClientInfoException sqlClientInfoException, ConnectionMember_I delegatedInstance) {
        ConnectionAnalysis.Recommendation recommendation =  exceptionHandler.handleSQLClientInfoException(sqlClientInfoException);
    }
    public IOException handleIoException(IOException ioException, ConnectionMember_I delegatedInstance) {
        ConnectionAnalysis.Recommendation recommendation = exceptionHandler.handleIoException(ioException);
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
        if (options.)
        this.notionConnectionTree.
    }

    protected abstract void setNotionConnectionTree(NotionConnectionDelegate notionConnectionTree);


}
