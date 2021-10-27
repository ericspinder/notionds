package com.notionds.dataSource.connection;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public class Cleanup<O extends Options> implements Runnable {

    private static final Logger log = LogManager.getLogger(Cleanup.class);

    protected final O options;
    protected final Consumer<ConnectionArtifact_I> returnConnectionFutureConsumer;
    private final Consumer<Boolean> failoverConsumer;
    protected boolean doCleanup = true;
    public final ReferenceQueue<ConnectionArtifact_I> globalReferenceQueue = new ReferenceQueue<>();
    public final Map<Container<?,?,?>, Instant> timeoutCleanup = Collections.synchronizedMap(new WeakHashMap<>());


    public Cleanup(O options, Consumer<ConnectionArtifact_I> returnConnectionFutureConsumer, Consumer<Boolean> failoverConsumer) {
        this.options = options;
        this.returnConnectionFutureConsumer = returnConnectionFutureConsumer;
        this.failoverConsumer = failoverConsumer;
    }

    /**
     * Patrol ConnectionContainer timeouts
     */
    protected void patrolTimeouts() {
        for (Map.Entry<Container<?,?,?>, Instant> containerInstantEntry: timeoutCleanup.entrySet()) {
            Instant expireTime = containerInstantEntry.getValue();
            if (expireTime != null && expireTime.isAfter(Instant.now())) {
                Container<?,?,?> container = containerInstantEntry.getKey();
                container.closeDelegate(container.getConnection());
                log.error("Timeout for ConnectionId=" + container.containerId);
            }
        }
    }

    /**
     * Check if any 'top level' Connection instances have been found in garbage collection
     */
    protected void sortGarbage() throws InterruptedException {
        @SuppressWarnings("unchecked")
        Reference<ConnectionArtifact_I> reference = (Reference<ConnectionArtifact_I>) globalReferenceQueue.remove();
        if (reference != null) {
            ConnectionArtifact_I artifact = reference.get();
            if (artifact != null) {
                if (artifact.equals(artifact.getContainer().getConnection()) && artifact.getContainer().currentState.equals(State.Open)) {
                    this.returnConnectionFutureConsumer.accept(artifact);
                    log.debug("returned from garbage collection: id={}, created={}", artifact.getContainer().containerId, artifact.getContainer().createInstant);
                }
                else {
                    log.debug("Garbage collection continuing on uuid=" + artifact.getArtifactId());
                    artifact.getContainer().closeDelegate(artifact);
                }
            }
        }
    }

    public void run() {
        try {
            while (doCleanup) {
                sortGarbage();
                patrolTimeouts();
            }
        }
        catch (InterruptedException ie) {
            return;
        }
    }

    public Consumer<ConnectionArtifact_I> getReturnConnectionFutureConsumer() {
        return returnConnectionFutureConsumer;
    }
    public Consumer<Boolean> getFailoverConsumer() {
        return this.failoverConsumer;
    }
}
