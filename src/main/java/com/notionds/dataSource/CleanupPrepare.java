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

public class CleanupPrepare implements Runnable {

    private static final Logger logger = LogManager.getLogger(CleanupPrepare.class);
    protected boolean doCleanup = true;
    private final ReferenceQueue<ConnectionArtifact_I<Connection>> connectionReferenceQueue = new ReferenceQueue<>();
    protected final Map<ConnectionContainer, Instant> timeoutCleanup = Collections.synchronizedMap(new WeakHashMap<>());

    protected final ConnectionPool connectionPool;

    public CleanupPrepare(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
    /**
     * Patrol ConnectionContainer timeouts
     */
    protected void patrolTimeouts() {
        logger.info("patrolTimeouts");
        for (Map.Entry<ConnectionContainer, Instant> containerInstantEntry: timeoutCleanup.entrySet()) {
            ConnectionContainer connectionContainer = containerInstantEntry.getKey();
            ConnectionArtifact_I<Connection> connectionArtifact = connectionContainer.get();
            if (connectionArtifact == null || connectionArtifact.getDelegate() == null) {
                logger.info("Removing dead ConnectionContainer id = " + connectionContainer.containerId);
                timeoutCleanup.remove(connectionContainer);
            }
            Instant expireTime = containerInstantEntry.getValue();
            if (expireTime != null && expireTime.isAfter(Instant.now()) && connectionContainer.getCurrentState().equals(State.Pooled)) {
                try {
                    connectionArtifact.getDelegate().close();
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
        logger.debug("Sort Garbage");
        Reference<?> reference = connectionReferenceQueue.remove();
        if (reference instanceof ConnectionContainer connectionContainer) {
            ConnectionArtifact_I<Connection> artifact = connectionContainer.get();
            if (artifact != null) {
                artifact.getConnectionContainer().getConnectionPool().returnConnection(artifact);
            }
        }
    }
    protected void patrolPoolUsage() {
        int totalSpaceAvailableFromMax = (int) connectionPool.getOptions().get(Options.Integers.Connection_Max_Queue_Size.getKey()) - connectionPool.loanedConnections.size() + connectionPool.connectionQueue.size();
        logger.trace("totalSpaceAvailableFromMax = " + totalSpaceAvailableFromMax + ", loanedConnection.size = " + connectionPool.loanedConnections.size() + ", connectionQueue = " + connectionPool.connectionQueue.size());
        if ((connectionPool.connectionQueue.size() < (int) connectionPool.getOptions().get(Options.Integers.Connections_Min_Active.getKey()) ||
                (connectionPool.loanedConnections.size() > (int) connectionPool.getOptions().get(Options.Integers.Connections_Max_Loaned_Out_Refill.getKey()) && connectionPool.connectionQueue.size() < (int) connectionPool.getOptions().get(Options.Integers.Connections_Min_Active.getKey()) )) &&
                totalSpaceAvailableFromMax > 0) {
            CompletableFuture.allOf(connectionPool.addConnectionFutures((int) connectionPool.getOptions().get(Options.Integers.Connections_Refill_Count.getKey())));
        }
    }
    public ReferenceQueue<ConnectionArtifact_I<Connection>> getConnectionReferenceQueue() {
        return this.connectionReferenceQueue;
    }

    public void run() {
        try {
            while (doCleanup) {
                logger.debug("doCleanup");
                sortGarbage();
                patrolTimeouts();
                patrolPoolUsage();
                logger.debug("finished cleanup");
            }
        }
        catch (InterruptedException ie) {
            return;
        }
    }

}
