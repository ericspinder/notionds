package com.notionds.dataSupplier.management;

import com.notionds.dataSupplier.NotionDs;
import com.notionds.dataSupplier.NotionStartupException;
import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.operational.Operational;

import javax.management.*;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Management<N, O extends Operational, W extends Wrapper<N>> {

    public static class Default_Unavailable extends Management<Operational.Default> {

        public static final Default_Unavailable INSTANCE = new Default_Unavailable();

        public Default_Unavailable() {
            super(Operational.DEFAULT_OPTIONS_INSTANCE);
        }

        @Override
        protected void initialize(String instanceName) {

        }
    }


    protected abstract void initialize(String instanceName);
    private final O options;
    private NotionDs<?,?,?,?> notionDs;


    /**
     * The max time a connection will be allowed to stay active.
     */
    protected Supplier<Duration> maxConnectionLifetime_getter = () -> notionDs.getConnectionPool().getMaxConnectionLifetime();
    protected Consumer<Duration> maxConnectionLifetime_setter = (Duration maxConnectionLifetime) -> notionDs.getConnectionPool().setMaxConnectionLifetime(maxConnectionLifetime);
    /**
     * Default timeout when loaned out
     */
    protected Supplier<Duration> timeOnLoan_getter = () -> notionDs.getConnectionPool().getTimeoutOnLoan_default();
    protected Consumer<Duration> timeOnLoan_setter = (Duration timeoutOnLoan_default) -> notionDs.getConnectionPool().setTimeoutOnLoan_default(timeoutOnLoan_default);
    /**
     * Duration split into TimeUnits for efficient use in the poll method
     */
    protected Supplier<Duration> connectionRetrieve_getter = () -> notionDs.getConnectionPool().getConnection_retrieveTimeout();
    protected Consumer<Duration> connectionRetrieve_setter = (Duration connection_retrieve_millis) -> notionDs.getConnectionPool().setConnection_retrieveTimeout(connection_retrieve_millis);
    /**
     * Max number of total connections allowed, this is not a hard limit, but will start rejecting returned connections above this number.
     */
    protected Supplier<Integer> maxTotalAllowedConnections_getter = () -> notionDs.getConnectionPool().getMaxTotalAllowedConnections();
    protected Consumer<Integer> maxTotalAllowedConnections_setter = (Integer maxTotalAllowedConnections) -> notionDs.getConnectionPool().setMaxTotalAllowedConnections(maxTotalAllowedConnections);
    /**
     * The number of connections the system will attempt to keep in absence of breaching the maximum
     */
    protected Supplier<Integer> minActiveConnections_getter = () -> notionDs.getConnectionPool().getMinActiveConnections();
    protected Consumer<Integer> minActiveConnections_setter = (Integer minActiveConnections) -> notionDs.getConnectionPool().setMinActiveConnections(minActiveConnections);

    public Management(O options) {
        this.options = options;
    }

    public void setNotionDs(NotionDs<?,?,?,?> notionDs, String instanceName) {
        if (this.notionDs == null) {
            this.notionDs = notionDs;
            this.initialize(instanceName);
        }
        else {
            throw new NotionStartupException(NotionStartupException.Type.UnableToChangeJXM, this.getClass());
        }
    }
    public MBeanInfo getMBeanInfo() {
        throw new UnsupportedOperationException("MBeanInfo was not overridden for Management Bean");
    }
}
