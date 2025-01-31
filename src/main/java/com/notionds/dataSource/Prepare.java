package com.notionds.dataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Prepare implements Runnable {

    private static final Logger logger = LogManager.getLogger(Prepare.class);
    protected boolean doCleanup = true;

    protected final ConnectionPool connectionPool;

    public Prepare(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Check if any 'top level' Connection instances have been found in garbage collection
     */
    protected void patrolPoolUsage() throws InterruptedException, ExecutionException {
        boolean maxConnectionNotHitYet = connectionPool.activeConnectionContainers.size() < (int) connectionPool.getOptions().get(Options.Integers.Connection_Max_Queue_Size.getKey());
        boolean availableConnectionsBelowMinNeeded = connectionPool.connectionQueue.size() < (int) connectionPool.getOptions().get(Options.Integers.Connections_Min_Active.getKey());
        //logger.trace("activeConnection.size = " + connectionPool.activeConnectionContainers.size() + ", connectionQueue = " + connectionPool.connectionQueue.size() + ", maxConnectionNotHitYet = " + maxConnectionNotHitYet + ", availableConnectionsBelowMinNeeded" + availableConnectionsBelowMinNeeded + ", connectionQueueSize = " + connectionPool.connectionQueue.size());
        if (maxConnectionNotHitYet && availableConnectionsBelowMinNeeded || connectionPool.connectionQueue.size() < 2) {
            logger.debug("Adding connection");
            CompletableFuture.allOf(connectionPool.addConnectionFutures(2)).get();
        }
        Thread.sleep(1000);

    }


    public void run() {
        try {
            while (doCleanup) {
                patrolPoolUsage();
            }
        }
        catch (InterruptedException | ExecutionException ie) {
            Thread.currentThread().interrupt();
        }
    }

}
