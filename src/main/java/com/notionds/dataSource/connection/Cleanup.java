package com.notionds.dataSource.connection;

import com.notionds.dataSource.ConnectionPool;
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

public abstract class Cleanup<O extends Options, P extends ConnectionPool> implements Runnable {

    public static final Cleanup.Default DEFAULT_INSTANCE = new Default();

    public static final class Default extends Cleanup<Options.Default, ConnectionPool.Default> {
        public Default() {
            super(Options.DEFAULT_OPTIONS_INSTANCE, ConnectionPool.DEFAULT_INSTANCE);
        }
    }

    private static final Logger log = LogManager.getLogger(Cleanup.class);

    protected final O options;
    protected final P connectionPool;
    protected boolean doCleanup = true;
    public final ReferenceQueue<ConnectionArtifact_I> globalReferenceQueue = new ReferenceQueue<>();
    public final Map<ConnectionContainer<?,?,?,?>, Instant> timeoutCleanup = Collections.synchronizedMap(new WeakHashMap<>());


    public Cleanup(O options, P connectionPool) {
        this.options = options;
        this.connectionPool = connectionPool;
    }

    public P getConnectionPool() {
        return this.connectionPool;
    }


    /**
     *
     * @throws InterruptedException
     */
    protected void globalCleanup() throws InterruptedException  {
        // First check if any 'top level' Connection instances have been found in garbage collection
        @SuppressWarnings("unchecked")
        Reference<ConnectionArtifact_I> reference = (Reference<ConnectionArtifact_I>) globalReferenceQueue.remove();
        if (reference != null) {
            ConnectionArtifact_I artifact = reference.get();
            if (artifact != null) {
                if (artifact.equals(artifact.getConnectionMain().getConnection()) && artifact.getConnectionMain().currentState.equals(State.Open)) {
                    connectionPool.addConnectionFuture(artifact, true);
                    log.debug("returned from garbage collection: id={}, created={}", artifact.getConnectionMain().containerId, artifact.getConnectionMain().createInstant);
                }
                else {
                    try {
                        artifact.closeDelegate();
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Error closing a delegate" + e);
                    }
                }
            }
        }

        for (Map.Entry<ConnectionContainer<?,?,?,?>, Instant> containerInstantEntry: timeoutCleanup.entrySet()) {
            Instant expireTime = containerInstantEntry.getValue();
            if (expireTime != null && expireTime.isAfter(Instant.now())) {
                ConnectionContainer<?,?,?,?> container = containerInstantEntry.getKey();
                container.closeChildren();
                try {
                    container.getConnection().closeDelegate();
                    log.error("Timeout for ConnectionId=" + container.containerId);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error closing the delegates on timeout ConnectionId=" + container.containerId + ", message=" + e.getMessage());
                }
            }
        }
    }
    public static void DoDelegateClose(Object delegate) {
        try {
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
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exception trying to clean up Reference was ignored: ");
            PrintCause(e, stringBuilder);
            log.error(stringBuilder.toString());
        }
    }
    protected static void PrintCause(Throwable t, StringBuilder stringBuilder) {
        stringBuilder.append(t.getMessage());
        if (t.getCause() != null) {
            stringBuilder.append("\n caused by: ");
            PrintCause(t.getCause(), stringBuilder);
        }
    }

    public void run() {
        try {
            while (doCleanup) {
                this.globalCleanup();
            }
        }
        catch (InterruptedException ie) {
            return;
        }
    }
}
