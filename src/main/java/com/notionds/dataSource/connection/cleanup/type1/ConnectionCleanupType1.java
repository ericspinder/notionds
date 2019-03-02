package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
import com.notionds.dataSource.connection.VendorConnection;
import com.notionds.dataSource.connection.cleanup.ConnectionCleanup;
import com.notionds.dataSource.connection.cleanup.NotionCleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.util.Map;
import java.util.WeakHashMap;

public class ConnectionCleanupType1<O extends Options, NC extends NotionCleanup, VC extends VendorConnection> extends ConnectionCleanup<O, NC, VC> {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionCleanupType1.class);

    private ConnectionMemberWeakReferenceType1 connectionWeakReference = null;
    private Map<ConnectionMember_I, ConnectionMemberWeakReferenceType1> allWeakReferences = new WeakHashMap<>();
    private final ReferenceQueue<ConnectionMember_I> referenceQueue;

    public ConnectionCleanupType1(O options, NC notionCleanup, VC vendorConnection, ReferenceQueue<ConnectionMember_I> referenceQueue) {
        super(options, notionCleanup, vendorConnection);
        this.referenceQueue = referenceQueue;
    }
    public Connection getConnection(ConnectionContainer connectionContainer) {
        if (this.connectionWeakReference != null) {
            return (Connection) this.connectionWeakReference.get();
        }
        else {
            return (Connection) connectionContainer.wrap(this.vendorConnection, Connection.class, null, null);
        }
    }


    public void closeAll() {
        if (this.connectionWeakReference != null ) {
            Map<ConnectionMemberWeakReferenceType1, Object> children = this.connectionWeakReference.getChildren();
        }
    }

    public ConnectionMember_I add(ConnectionMember_I connectionMember, Object delegate, ConnectionMember_I parent) {
        ConnectionMemberWeakReferenceType1 weakReference = new ConnectionMemberWeakReferenceType1(connectionMember, delegate, this.referenceQueue);
        if (this.connectionWeakReference == null && parent == null && connectionMember instanceof Connection) {
            this.connectionWeakReference = weakReference;
        }
        else {
            this.allWeakReferences.get(parent).addChild(weakReference);
        }
        this.allWeakReferences.put(connectionMember, weakReference);
        return connectionMember;
    }

    public void close(Connection connection, boolean closeConnectionDelegate) {
        try {
            ConnectionMember_I connectionMember = (ConnectionMember_I) connection;
            if (this.connectionWeakReference.get() != connection) {
                throw new RuntimeException("Connection object wasn't the one in this isntance (must be a ConnectionMember_I object from this instance");
            }

            Map<ConnectionMemberWeakReferenceType1, Object> children = this.connectionWeakReference.getChildren();
            this.closeAllChildren(children);

            if (!this.options.get(Options.NotionDefaultBooleans.ConnectionPool_Use) || closeConnectionDelegate) {
                this.closeAndClear(this.connectionWeakReference);
            }
            else {
                this.connectionWeakReference.clear();

            }
        }
        catch (ClassCastException cce) {
            throw new RuntimeException("Connection object wasn't the one in this isntance (must be a ConnectionMember_I object from this instance");
        }
    }
    public void close(Connection connection) {
        this.close(connection, false);
    }
    public void close(ConnectionMember_I connectionMember) {
        ConnectionMemberWeakReferenceType1 weakReference = this.allWeakReferences.get(connectionMember);
        if (weakReference != null) {
            this.close(weakReference);
        }
    }
    private void close(ConnectionMemberWeakReferenceType1 weakReference) {
        Map<ConnectionMemberWeakReferenceType1, Object> children = weakReference.getChildren();
        if (!children.isEmpty()) {
            this.closeAllChildren(children);
        }
        this.closeAndClear(weakReference);
        this.allWeakReferences.remove(weakReference.get());

    }
    private void closeAllChildren(Map<ConnectionMemberWeakReferenceType1, Object> children) {
        for (ConnectionMemberWeakReferenceType1 weakReferenceChild: children.keySet()) {
            Map<ConnectionMemberWeakReferenceType1, Object> weakReferenceChildren = weakReferenceChild.getChildren();
            if (!children.isEmpty()) {
                this.closeAllChildren(weakReferenceChildren);
            }
            this.close(weakReferenceChild);
        }
    }

    private void closeAndClear(ConnectionMemberWeakReferenceType1 notionReference) {
        try {
            Object delegate = notionReference.getDelegate();
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
        }
        catch (Exception e) {
            printCause(e);
        }
        finally {
            ((Reference) notionReference).clear();
        }
    }

    private final Throwable printCause(Throwable t) {
        logger.info("Expection trying to clean up Reference was ignorned: " + t.getMessage());
        if (t.getCause() != null) {
            printCause(t.getCause());
        }
        return null;
    }
}
