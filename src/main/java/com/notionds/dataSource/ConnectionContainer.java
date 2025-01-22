package com.notionds.dataSource;

import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.WrapperFactory_I;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.ref.SoftReference;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class ConnectionContainer extends SoftReference<ConnectionArtifact_I<Connection>> implements Comparable<ConnectionContainer> {

    private static final Logger logger = LogManager.getLogger(ConnectionContainer.class);
    public final UUID containerId = UUID.randomUUID();
    public final Instant createInstant = Instant.now();
    private final WrapperFactory_I connectionWrapper;
    private final ConnectionPool connectionPool;
    protected volatile State currentState;
    private final Duration connectionChildTimeout;
    private final Map<Object,Instant> connectionChildren = new WeakHashMap<>();

    public ConnectionContainer(ConnectionArtifact_I<Connection> connection, ConnectionPool connectionPool, WrapperFactory_I connectionWrapper, Duration connectionChildTimeout) {
        super(connection,connectionPool.getCleanup().getConnectionReferenceQueue());
        this.connectionPool = connectionPool;
        this.connectionWrapper = connectionWrapper;
        this.connectionChildTimeout = connectionChildTimeout;
        this.currentState = State.Pooled;
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





}
