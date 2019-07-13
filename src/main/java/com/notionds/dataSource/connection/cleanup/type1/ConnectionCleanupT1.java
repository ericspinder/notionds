package com.notionds.dataSource.connection.cleanup.type1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.Recommendation;
import com.notionds.dataSource.connection.ConnectionMain;
import com.notionds.dataSource.connection.VendorConnection;
import com.notionds.dataSource.connection.cleanup.ConnectionCleanup;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class ConnectionCleanupT1<O extends Options, VC extends VendorConnection> extends ConnectionCleanup<O, VC> {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionCleanupT1.class);

    private ConnectionWR connectionWeakReference = null;
    private final Map<ConnectionMember_I, ConnectionMemberWR> allConnectionMemberWR = Collections.synchronizedMap(new WeakHashMap<>());
    private final ReferenceQueue<ConnectionMember_I> connectionMemberRQ;

    public ConnectionCleanupT1(O options, VC vendorConnection, ReferenceQueue<ConnectionMember_I> connectionMemberRQ) {
        super(options, vendorConnection);
        this.connectionMemberRQ = connectionMemberRQ;
    }

    @Override
    public void cleanupAll() {
        this.connectionWeakReference.closeDelegate();
        this.vendorConnection.release(Recommendation.ReturnToPool);
    }

    public ConnectionWR getConnectionWeakReference() {
        return this.connectionWeakReference;
    }

    @Override
    public Connection getConnection(ConnectionMain connectionMain) {
        if (this.connectionWeakReference != null) {
            return (Connection) this.connectionWeakReference.get();
        }
        else {
            return (Connection) connectionMain.wrap(this.vendorConnection.getDelegate(), Connection.class, null, null);
        }
    }

    @Override
    public ConnectionMember_I add(ConnectionMember_I connectionMember, Object delegate, ConnectionMember_I parent) {
        if (this.connectionWeakReference == null && parent == null && connectionMember instanceof Connection) {
            this.connectionWeakReference = new ConnectionWR(connectionMember, connectionMemberRQ, (Connection) delegate, this.allConnectionMemberWR);
        }
        else {
            ConnectionMemberWR weakReference = new ConnectionMemberWR(connectionMember, this.connectionMemberRQ, delegate);
            this.allConnectionMemberWR.put(connectionMember, weakReference);
        }
        return connectionMember;
    }

    @Override
    public void clear(Connection connection) {
        this.connectionWeakReference.clear();
    }

    @Override
    public void clear(ConnectionMember_I connectionMember) {
        WeakReference<ConnectionMember_I> weakReference = this.allConnectionMemberWR.get(connectionMember);
        if (weakReference != null) {
            weakReference.clear();
        }
    }
}
