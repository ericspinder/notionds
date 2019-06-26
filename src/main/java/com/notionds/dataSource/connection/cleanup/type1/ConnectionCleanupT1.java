package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.VendorConnection;
import com.notionds.dataSource.connection.cleanup.ConnectionCleanup;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class ConnectionCleanupT1<O extends Options, VC extends VendorConnection> extends ConnectionCleanup<O, VC> {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionCleanupT1.class);

    private WeakConnectionMember connectionWeakReference = null;
    private Map<ConnectionMember_I, WeakConnectionMember> allConnectionMemberWR = Collections.synchronizedMap(new WeakHashMap<>());
    private final ReferenceQueue<ConnectionMember_I> referenceQueue;

    public ConnectionCleanupT1(O options, VC vendorConnection) {
        super(options, vendorConnection);
    }
    @Override
    public Connection getConnection(ConnectionContainer connectionContainer) {
        if (this.connectionWeakReference != null) {
            return (Connection) this.connectionWeakReference.get();
        }
        else {
            return (Connection) connectionContainer.wrap(this.vendorConnection.getDelegate(), Connection.class, null, null);
        }
    }

    @Override
    public ConnectionMember_I add(ConnectionMember_I connectionMember, Object delegate, ConnectionMember_I parent) {
        WeakConnectionMember weakReference = new WeakConnectionMember(connectionMember, delegate, this.referenceQueue);
        if (this.connectionWeakReference == null && parent == null && connectionMember instanceof Connection) {
            this.connectionWeakReference = weakReference;
        }
        else {
            this.allConnectionMemberWR.get(parent).addChild(weakReference);
        }
        this.allConnectionMemberWR.put(connectionMember, weakReference);
        return connectionMember;
    }

    @Override
    public void cleanup(boolean closeConnectionDelegate) {
        try {
            Map<WeakConnectionMember, Object> children = this.connectionWeakReference.getChildren();
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

    @Override
    public void cleanup(ConnectionMember_I connectionMember) {
        WeakConnectionMember weakReference = this.allConnectionMemberWR.get(connectionMember);
        if (weakReference != null) {
            this.cleanup(weakReference);
        }
    }
    private void cleanup(WeakConnectionMember weakReference) {
        Map<WeakConnectionMember, Object> children = weakReference.getChildren();
        if (!children.isEmpty()) {
            this.closeAllChildren(children);
        }
        this.closeAndClear(weakReference);
        this.allConnectionMemberWR.remove(weakReference.get());

    }
    private void closeAllChildren(Map<WeakConnectionMember, Object> children) {
        for (WeakConnectionMember weakReferenceChild: children.keySet()) {
            Map<WeakConnectionMember, Object> weakReferenceChildren = weakReferenceChild.getChildren();
            if (!children.isEmpty()) {
                this.closeAllChildren(weakReferenceChildren);
            }
            this.cleanup(weakReferenceChild);
        }
    }

    private void closeAndClear(WeakConnectionMember notionReference) {
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
