package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.OperationAccounting;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.*;
import com.notionds.dataSource.connection.manual9.*;
import com.notionds.dataSource.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.locks.StampedLock;

public abstract class ConnectionContainer<O extends Options, W extends WrapperOfNotion> {

    private final Logger logger = LoggerFactory.getLogger(ConnectionContainer.class);

    private NotionWeakReference notionConnectionTree = null;
    private StampedLock weakWrapperCloseGate = new StampedLock();
    private final O options;
    private final UUID connectionId = UUID.randomUUID();
    private final VendorConnection vendorConnection;
    private final W wrapper;
    private ReferenceQueue<ConnectionMember_I> referenceQueue = new ReferenceQueue<>();

    public ConnectionContainer(O options, VendorConnection vendorConnection) {
        this.options = options;
        this.vendorConnection = vendorConnection;
        try {
            this.wrapper = ((Class<W>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredConstructor().newInstance(options);
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot create WrapperOfNotion: " + e.getMessage());
        }
    }
    public Connection getNotionConnection() {
        if (this.notionConnectionTree == null) {
            return (Connection) this.getNotionConnectionWeakReference().get();
        }
        return (Connection) this.notionConnectionTree.get();
    }
    public NotionWeakReference getNotionConnectionWeakReference() {
        if (this.notionConnectionTree == null) {
            try {
                this.notionConnectionTree = new NotionWeakReference(this.wrap(vendorConnection.getDelegate()), this.vendorConnection.getDelegate());
            } catch (Exception e) {
                throw new RuntimeException("Problem creating NotionConnection_Keep_for_now instance " + e.getMessage());
            }
        }
        return this.notionConnectionTree;
    }
    public ReferenceQueue<ConnectionMember_I> getReferenceQueue() {
        return this.referenceQueue;
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
    public VendorConnection getVendorConnection() {
        return this.vendorConnection;
    }
    public Connection getUnderlyingVendorConnection() {
        return this.vendorConnection.getDelegate();
    }
    public final UUID getConnectionId() {
        return this.connectionId;
    }

    public void close(ConnectionMember_I delegatedInstance) {

    }

    protected NotionWeakReference getFirstChild()
    public void closeAllChildren() {
        if (this.notionConnectionTree != null ) {
            Map<NotionWeakReference, Object> children = this.notionConnectionTree.getChildren();
        }
    }
    protected void closeAllChildren(NotionWeakReference notionWeakReference) {


    }
    protected NotionWeakReference getFirstChild(Map<NotionWeakReference, Object> map) {
        for (NotionWeakReference notionWeakReference: map.keySet()) {
            if (notionWeakReference.getChildren().isEmpty()) {
                return notionWeakReference;
            }
            this.getFirstChild();
        }
    }
    protected boolean closeAndClear(NotionWeakReference notionWeakReference, boolean forceClose) {
        long stamp = weakWrapperCloseGate.writeLock();
        try {
            ConnectionMember_I child = (ConnectionMember_I) notionWeakReference.get();
            if (child != null && !State.Closed.equals(child.getState())) {
                try {
                    Object delegate = notionWeakReference.getDelegate();
                    if (!forceClose && options.get(Options.NotionDefaultBooleans.ConnectionContainer_Check_ResultSet)) {
                        if (delegate instanceof ResultSet) {
                            if (((ResultSet) delegate).isBeforeFirst()) {
                                // isBeforeFirst will be true when it hasn't yet been 'nexted' or more importantly when the ResultSet is finished
                                return false;
                            }
                        }
                    }
                    if (delegate instanceof AutoCloseable) {
                        ((AutoCloseable)delegate).close();
                    }
                    else if (delegate instanceof Clob) {
                        ((Clob)delegate).free();
                    }
                    else if (delegate instanceof Blob) {
                        ((Blob)delegate).free();
                    }
                    else if (delegate instanceof Array) {
                        ((Array)delegate).free();
                    }
                } catch (SQLClientInfoException sqle) {
                    this.handleSQLClientInfoExcpetion(sqle, child);
                } catch (SQLException sqle) {
                    this.handleSQLException(sqle, child);
                } catch (IOException ioe) {
                    this.handleIoException(ioe, child);
                } catch (Exception e) {
                    this.handleException(e, child);
                } finally {
                    child.setState(State.Closed);
                }
            }
            notionWeakReference.clear();
            return true;
        }
        finally {
            weakWrapperCloseGate.unlockWrite(stamp);
        }
    }
    public void closeNotionConnectionTree() {
        if (this.notionConnectionTree != null ) {
            NotionConnection_Keep_for_now notionConnection = this.notionConnectionTree.get();
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

    protected ConnectionMember_I wrap(Object delegate) {
        ConnectionMember_I connectionDelegate = wrapper.createDelegate(delegate);
        NotionWeakReference weakReference = new NotionWeakReference(connectionDelegate, delegate, this);
        return connectionDelegate;
    }


}
