package com.notionds.dataSource;

import com.notionds.dataSource.connection.State;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CleanupPrepare implements Runnable {

    private static final Logger logger = LogManager.getLogger(CleanupPrepare.class);
    protected boolean doCleanup = true;
    private final ReferenceQueue<ConnectionArtifact_I<Connection>> connectionReferenceQueue = new ReferenceQueue<>();
    protected final Map<ConnectionContainer, Instant> timeoutCleanup = Collections.synchronizedMap(new WeakHashMap<>());

    protected final ConnectionPool connectionPool;

    public CleanupPrepare(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        logger.info("CleanupPrepare");
    }
    /**
     * Patrol ConnectionContainer timeouts
     */
    protected void patrolTimeouts() {
        for (Map.Entry<ConnectionContainer, Instant> containerInstantEntry: timeoutCleanup.entrySet()) {
            ConnectionContainer connectionContainer = containerInstantEntry.getKey();
            ConnectionArtifact_I<?> connectionArtifact = connectionContainer.get();
            if (connectionArtifact == null || connectionArtifact.getDelegate() == null) {
                logger.info("Removing dead ConnectionContainer id = " + connectionContainer.containerId);
                timeoutCleanup.remove(connectionContainer);
            }
            Instant expireTime = containerInstantEntry.getValue();
            if (expireTime != null && expireTime.isAfter(Instant.now()) && connectionContainer.getCurrentState().equals(State.Pooled)) {
                try {
                    assert connectionArtifact != null;
                    ((Connection)connectionArtifact.getDelegate()).close();
                } catch (SQLException e) {
                    logger.error("problem on close" + e.getMessage());
                }
                timeoutCleanup.remove(connectionContainer);
                logger.info("Timeout for ConnectionId = " + connectionContainer.containerId);
            }
        }
    }

    /**
     * Check if any 'top level' Connection instances have been found in garbage collection
     */
    protected void sortGarbage() throws InterruptedException {
        Reference<?> reference = connectionReferenceQueue.poll();
        if (reference instanceof ConnectionContainer connectionContainer) {
            ConnectionArtifact_I<?> artifact = connectionContainer.get();
            if (artifact != null) {
                if (connectionContainer.getConnectionPool().returnConnection(connectionContainer)) {
                    logger.trace("returning connection, artifactId = " + artifact.getArtifactId());
                }
                else {
                    logger.info("DID NOT return connection, artifactId = " + artifact.getArtifactId());
                }
            }
        }
    }
    protected void patrolPoolUsage() throws InterruptedException, ExecutionException {
        Thread.sleep(1000);
        boolean maxConnectionNotHitYet = connectionPool.loanedConnections.size() + connectionPool.connectionQueue.size() < (int) connectionPool.getOptions().get(Options.Integers.Connection_Max_Queue_Size.getKey());
        boolean availableConnectionsBelowMinNeeded = connectionPool.connectionQueue.size() < (int) connectionPool.getOptions().get(Options.Integers.Connections_Min_Active.getKey());
        //logger.trace("loanedConnection.size = " + connectionPool.loanedConnections.size() + ", connectionQueue = " + connectionPool.connectionQueue.size() + ", maxConnectionNotHitYet = " + maxConnectionNotHitYet + ", availableConnectionsBelowMinNeeded" + availableConnectionsBelowMinNeeded);
        if (maxConnectionNotHitYet && availableConnectionsBelowMinNeeded) {
            CompletableFuture.allOf(connectionPool.addConnectionFutures(1)).get();
        }
    }

    public ReferenceQueue<ConnectionArtifact_I<Connection>> getConnectionReferenceQueue() {
        return this.connectionReferenceQueue;
    }

    public void run() {
        try {
            while (doCleanup) {
                sortGarbage();
                patrolTimeouts();
                patrolPoolUsage();
            }
        }
        catch (InterruptedException | ExecutionException ie) {
            Thread.currentThread().interrupt();
        }
    }

}
