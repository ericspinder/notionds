package com.notionds.dataSource.connection.cleanup;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class GlobalCleanup<O extends Options> implements Runnable {

    private static final Logger log = LogManager.getLogger(GlobalCleanup.class);

    protected final O options;
    protected boolean doCleanup = true;
    public final ReferenceQueue<ConnectionArtifact_I> globalReferenceQueue = new ReferenceQueue<>();
    public final Map<ConnectionContainer, Instant> timeoutCleanup = Collections.synchronizedMap(new WeakHashMap<>());

    @SuppressWarnings("unchecked")
    public GlobalCleanup(O options) {
        this.options = options;
    }
    /**
     *
     * @throws InterruptedException
     */
    protected void globalCleanup() throws InterruptedException  {
        // First check if any 'top level' Connection instances have been found in garbage collection
        Reference reference = globalReferenceQueue.remove();
        if (reference) {
            log.info(gcConnectionMember.toString() + "was closed");
        }

        for (Map.Entry<ConnectionContainer, Instant> containerInstantEntry: timeoutCleanup.entrySet()) {
            Instant expireTime = containerInstantEntry.getValue();
            if (expireTime != null && expireTime.isAfter(Instant.now())) {
                ConnectionContainer container = containerInstantEntry.getKey();
                container.closeChildren();
                container.
            }
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
