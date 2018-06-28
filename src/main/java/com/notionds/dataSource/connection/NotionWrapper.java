package com.notionds.dataSource.connection;

import com.notionds.dataSource.ExceptionAdvice;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.manual9.*;
import com.notionds.dataSource.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.StampedLock;

public abstract class NotionWrapper<O extends Options, NC extends NotionConnection> {

    private final Logger logger = LoggerFactory.getLogger(NotionWrapper.class);
    private final Class<NC> notionConnectionClass = ((Class<NC>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);

    private NotionConnectionWeakReference<NC> notionConnectionTree = null;
    private final O options;
    private final UUID connectionId = UUID.randomUUID();
    private final VendorConnection vendorConnection;
    private StampedLock notionConnectionTreeGate = new StampedLock();

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
        long stamp = notionConnectionTreeGate.readLock();
        try {
            if (this.notionConnectionTree == null) {
                try {
                    this.notionConnectionTree = new NotionConnectionWeakReference(notionConnectionClass.getDeclaredConstructor(getClass()).newInstance(this), this);
                } catch (Exception e) {
                    throw new RuntimeException("Problem creating NotionConnection instance " + e.getMessage());
                }
            }
            return this.notionConnectionTree;
        }
        finally {
            notionConnectionTreeGate.unlockRead(stamp);
        }
    }

    protected void handleRecommendation(ExceptionAdvice.Recommendation recommendation) {
        //if (ExceptionAdvice.Recommendation.CloseConnectionInstance.equals(recommendation)) {

        //}
    }
    public SQLException handleSQLException(SQLException sqlException, ConnectionMember_I delegatedInstance) {
        Instant exceptionTime = Instant.now();
        ExceptionAccounting exceptionAccounting = new ExceptionAccounting(this.connectionId, this.vendorConnection.getDatabaseMain().remedySQLException(sqlException, this), delegatedInstance.getCreateInstant(), exceptionTime);
        this.vendorConnection.getConnectionAnalysis().addProblem(exceptionAccounting);
        return new SqlExceptionWrapper(exceptionAccounting, sqlException);
    }
    public SQLClientInfoException handleSQLClientInfoExcpetion(SQLClientInfoException sqlClientInfoException, ConnectionMember_I delegatedInstance) {
        Instant exceptionTime = Instant.now();
        ExceptionAccounting exceptionAccounting = new ExceptionAccounting(this.connectionId, this.vendorConnection.getDatabaseMain().remedySQLClientInfoException(sqlClientInfoException, this), delegatedInstance.getCreateInstant(), exceptionTime);
        this.vendorConnection.getConnectionAnalysis().addProblem(exceptionAccounting);
        return new SqlClientInfoExceptionWrapper(exceptionAccounting, sqlClientInfoException);
    }
    public IOException handleIoException(IOException ioException, ConnectionMember_I delegatedInstance) {
        Instant exceptionTime = Instant.now();
        ExceptionAccounting exceptionAccounting = new ExceptionAccounting(this.connectionId, this.vendorConnection.getDatabaseMain().remedyIoException(ioException, this), delegatedInstance.getCreateInstant(), exceptionTime);
        this.vendorConnection.getConnectionAnalysis().addProblem(exceptionAccounting);
        return new IoExceptionWrapper(exceptionAccounting, ioException);
    }
    public Exception handleException(Exception exception, ConnectionMember_I delegatedInstance) {
        Instant exceptionTime = Instant.now();
        ExceptionAccounting exceptionAccounting = new ExceptionAccounting(this.connectionId, this.vendorConnection.getDatabaseMain().remedyException(exception, this), delegatedInstance.getCreateInstant(), exceptionTime);
        this.vendorConnection.getConnectionAnalysis().addProblem(exceptionAccounting);
        return new ExceptionWrapper(exceptionAccounting, exception);
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
    public void closeAllChildren() {
        if (this.notionConnectionTree != null ) {
            Map<NotionWeakReference, State> children = this.notionConnectionTree.getChildren();
            Iterator<NotionWeakReference> childRefIt = children.keySet().iterator();
            while (childRefIt.hasNext()) {
                NotionWeakReference childRef = childRefIt.next();
                ConnectionMember_I  child = (ConnectionMember_I) childRef.get();
                if (child != null && !State.Closed.equals(child.getState())) {
                    try {
                        child.closeDelegate();
                    }
                    catch(SQLClientInfoException sqle) {
                        this.handleSQLClientInfoExcpetion(sqle, child);
                    }
                    catch(SQLException sqle) {
                        this.handleSQLException(sqle, child);
                    }
                    catch(IOException ioe) {
                        this.handleIoException(ioe, child);
                    }
                    catch (Exception e) {
                        this.handleException(e, child);
                    }
                    finally {
                        child.setState(State.Closed);
                    }
                }
                childRef.clear();
                childRefIt.remove();
            }
        }
    }
    public void closeNotionConnectionTree() {
        if (this.notionConnectionTree != null ) {
            NotionConnection notionConnection = this.notionConnectionTree.get();
            if (!State.Closed.equals(notionConnection.getState())) {
                try {
                    if (!notionConnection.isClosed()) {
                        notionConnection.closeDelegate();
                    }
                }
                catch(SQLException sqle) {
                    this.handleSQLException(sqle, notionConnection);
                }
                catch(IOException ioe) {
                    this.handleIoException(ioe, notionConnection);
                }
                catch (Exception e) {
                    this.handleException(e, notionConnection);
                }
                finally {

                }
            }
            this.notionConnectionTree.clear();
            this.notionConnectionTree = null;
        }
    }

    protected abstract void setNotionConnectionTree(NotionConnectionDelegate notionConnectionTree);


}
