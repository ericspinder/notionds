package com.notionds.dataSource;

import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.WrapperFactory_I;

import java.lang.ref.SoftReference;
import java.sql.Connection;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionContainer extends SoftReference<ConnectionArtifact_I<Connection>> implements Comparable<ConnectionContainer> {

    public final UUID containerId = UUID.randomUUID();
    public final Instant createInstant = Instant.now();
    private final WrapperFactory_I connectionWrapper;
    private final ConnectionPool connectionPool;
    protected volatile State currentState;
    private final UUID getConnectionSupplierUUID;
    private final AtomicInteger count = new AtomicInteger();

    public ConnectionContainer(UUID connectionSupplierUUID, ConnectionArtifact_I<Connection> connection, ConnectionPool connectionPool, WrapperFactory_I connectionWrapper) {
        super(connection,connectionPool.getCleanupPrepare().getConnectionReferenceQueue());
        this.getConnectionSupplierUUID = connectionSupplierUUID;
        this.connectionPool = connectionPool;
        this.connectionWrapper = connectionWrapper;
        this.currentState = State.Pooled;
    }
    public UUID getContainerId() {
        return containerId;
    }
    public UUID getGetConnectionSupplierUUID() {
        return getConnectionSupplierUUID;
    }
    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public WrapperFactory_I getConnectionWrapper() {
        return connectionWrapper;
    }

    @Override
    public int compareTo(ConnectionContainer that) {
        if (this.containerId.equals(that.containerId)) {
            return 0;
        } else {
            return (this.createInstant.compareTo(that.createInstant) == 0) ?
                    this.containerId.compareTo(that.containerId) :
                    this.createInstant.compareTo(that.createInstant);
        }
    }
    public State getCurrentState() {
        return this.currentState;
    }

    public int addReUse() {
        return this.count.addAndGet(1);
    }





}
