package com.notionds.dataSupplier.pool;

import com.notionds.dataSupplier.Container;
import com.notionds.dataSupplier.NotionStartupException;
import com.notionds.dataSupplier.Controller;
import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.exceptions.Recommendation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static com.notionds.dataSupplier.operational.DurationOption.*;
import static com.notionds.dataSupplier.operational.IntegerOption.*;

public abstract class Pool<N, O extends Operational, W extends Wrapper<N>> {

    private static final Logger log = LogManager.getLogger();
    protected final O options;
    private final Executor connectionExecutor;
    private final Controller<N,O,W,?,?,?,?,?> controller;
    /**
     * Default timeout when loaned out
     */
    private Duration timeoutOnLoan_default;
    /**
     * This is the length of time a client will wait for a notion to be created before erring out
     * The Duration is split into TimeUnits for efficient use in the poll method
     */
    private long connection_retrieve_millis;
    private final TimeUnit connection_retrieve_time_unit = TimeUnit.MILLISECONDS;
    /**
     * Max number of connections allowed, this is not a hard limit
     */
    private int maxTotalAllowedConnections;
    /**
     * The number of connections the system will attempt to keep in absence of breaching the maximum
     */
    private int minActiveConnections;
    /**
     * Holds the ready connection objects, wrapped and active
     */
    private final BlockingQueue<N> connectionQueue = new LinkedBlockingQueue<>();
    /**
     * The loaned Notions are held weakly and will drop out when garbage collected. They will be sent to a
     * referenceQueue in the Cleanup class when ready
     */
    private final WeakHashMap<W, Instant> loanedNotions = new WeakHashMap<>();

    public Pool(O options, Executor connectionExecutor, Controller<N,O,W,?,?,?,?,?> controller) {
        this.options = options;
        this.connectionExecutor = connectionExecutor;
        this.controller = controller;
        this.timeoutOnLoan_default = options.getDuration(ConnectionTimeoutOnLoan.getI18n());
        this.connection_retrieve_millis = (options.getDuration(RetrieveNewMember.getI18n()).toMillis());
        this.maxTotalAllowedConnections = options.getInteger(Connection_Max_Queue_Size.getI18n());
        this.minActiveConnections = options.getInteger(Connections_Min_Active.getI18n());
    }

    @SuppressWarnings("unchecked")
    private W newConnectionContainer() {
        return this.connectionPool.populateConnectionContainer();
    }


    protected populateConnectionContainer() {
        Container<,?,?> container = controller.getNewContainer(this.options, this, t, W artifactWrapper);
        try {
            return container.wrap(controller.getMemberSupplier(), Connection.class, null);
        } catch (Exception e) {
            r.handleException(e, null);
            return null;
        }
    }

    private boolean evalForSpaceInPool() {
        return loanedNotions.size() + connectionQueue.size() < maxTotalAllowedConnections;
    }

    @SuppressWarnings("unchecked")
    public W loanPooledMember(Supplier<Container<N,O,?>> newConnectionArtifactSupplier) {
        if (evalForSpaceInPool()) {
            addNotionFutures(newConnectionArtifactSupplier,minActiveConnections - connectionQueue.size());
        }
        try {
            W wrapper = (W) connectionQueue.poll(connection_retrieve_millis, connection_retrieve_time_unit);
            loanedNotions.put(wrapper, Instant.now());
            return wrapper;
        }
        catch (ClassCastException cce) {
            cce.printStackTrace();
            throw new NotionStartupException(NotionStartupException.Type.BadCastToGeneric, this.getClass());
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new NotionStartupException(NotionStartupException.Type.WAITED_TOO_LONG_FOR_CONNECTION, this.getClass());
        }
    }

    public boolean addNotion(W wrapper, boolean returned) {
        if (returned) {
            this.loanedNotions.remove(wrapper);
            if (!(wrapper.getContainer().getBridge().returnToPool(wrapper))) {
                log.error("ConnectionId=" + ((Wrapper)added).getContainer().containerId + " was not able to reuse, will close");
                (wrapper.getContainer().closeDelegate());
                return false;
            }
        }
        if (evalForSpaceInPool() && connectionQueue.add(added)) {
            ((Wrapper)added).getContainer().currentSituation = Situation.Pooled;
            log.debug("ConnectionId=" + ((Wrapper)added).getContainer().containerId + " was added/re-added to Notion pool, queue_size=" + connectionQueue.size());
            return true;
        } else {
            log.error("ConnectionId=" + ((Wrapper)added).getContainer().containerId + " was not able to add Notion pool, queue_size=" + connectionQueue.size());
            ((Wrapper)added).getContainer().closeDelegate(((Wrapper)added));
            return false;
        }
    }

    public void addNotionFutures(Supplier<Container<N,O,?>> newNotionSupplier, int notionsToAdd) {
        if (notionsToAdd >= options.getInteger(Minimum_Replenishment.getI18n())) {
            for (int i = 0; i < notionsToAdd; i++) {
                CompletableFuture.supplyAsync(() -> this.addNotion(newNotionSupplier.get(), false), connectionExecutor);
            }
        }
        else {
            log.debug("No need to fill notionsToAdd=" + notionsToAdd);
        }
    }
    public boolean addNotionNow(Supplier<Container<N,O,?>> newNotionSupplier) {
        return this.addNotion(newNotionSupplier.get(), false);
    }

    /**
     * Drains and closes the current connection pool and marks them all loaned to be close when no longer in use, rather than returned to the pool.
     */
    public void drainAllCurrentConnections() {
        List<Wrapper> drain = new ArrayList<>();
        this.connectionQueue.drainTo(drain);
        this.loanedNotions.keySet().stream().forEach((Wrapper loaned) -> loaned.getContainer().currentSituation = Situation.Empty);
        drain.forEach((Wrapper artifactI) -> artifactI.getContainer().closeDelegate(artifactI));

    }
    public void doFailover(Recommendation recommendation) {

    }

    public Duration getTimeoutOnLoan_default() {
        return timeoutOnLoan_default;
    }

    public void setTimeoutOnLoan_default(Duration timeoutOnLoan_default) {
        this.timeoutOnLoan_default = timeoutOnLoan_default;
    }

    public Duration getConnection_retrieveTimeout() {
        return Duration.of(connection_retrieve_millis, ChronoUnit.MILLIS);
    }

    public void setConnection_retrieveTimeout(Duration connection_retrieveTimeout) {
        this.connection_retrieve_time_unit.toMillis(connection_retrieveTimeout.toMillis());
    }

    public int getMaxTotalAllowedConnections() {
        return maxTotalAllowedConnections;
    }

    public void setMaxTotalAllowedConnections(int maxTotalAllowedConnections) {
        this.maxTotalAllowedConnections = maxTotalAllowedConnections;
    }

    public int getMinActiveConnections() {
        return minActiveConnections;
    }

    public void setMinActiveConnections(int minActiveConnections) {
        this.minActiveConnections = minActiveConnections;
    }
}
