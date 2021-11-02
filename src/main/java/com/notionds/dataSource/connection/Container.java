package com.notionds.dataSource.connection;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.Recommendation;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.AbstractConnectionWrapperFactory;
import com.notionds.dataSource.exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Container<O extends Options,
        A extends Advice,
        W extends AbstractConnectionWrapperFactory> implements Comparable<Container> {

    private static final Logger log = LogManager.getLogger(Container.class);

    private final O options;
    public final UUID containerId = UUID.randomUUID();
    public final Instant createInstant = Instant.now();
    private final A exceptionAdvice;
    private final W connectionWrapper;
    private final Cleanup<?> cleanup;
    public volatile State currentState;
    private SoftReference<ConnectionArtifact_I> connectionSoftReference;
    private Map<SoftReference<ConnectionArtifact_I>, Instant> connectionChildren = new HashMap<>();

    public Container(O options, A exceptionAdvice, W connectionWrapper, Cleanup<?> cleanup) {
        this.options = options;
        this.exceptionAdvice = exceptionAdvice;
        this.connectionWrapper = connectionWrapper;
        this.cleanup = cleanup;
        this.currentState = State.New_Needs_Connection;
    }

    @SuppressWarnings("unchecked")
    public Connection checkoutFromPool(Duration connectionTimeout) {
        if (connectionTimeout != null && !connectionTimeout.isNegative() && !connectionTimeout.isZero()) {
            this.cleanup.timeoutCleanup.put(this, Instant.now().plus(connectionTimeout) );
        }
        this.currentState = State.Open;
        Connection connection = (Connection) this.connectionSoftReference.get();
        try {
            connection.beginRequest();
            return connection;
        }
        catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exception trying to beginRequest() was ignored: ");
            PrintCause(e, stringBuilder);
            log.error(stringBuilder.toString());
            return null;
        }
    }
    public boolean reuse(ConnectionArtifact_I artifact) {
        log.debug("checking if to reuse connection Artifact=" + artifact.getArtifactId());
        this.cleanup.timeoutCleanup.remove(this);
        if (this.currentState.equals(State.Open) && artifact.equals(this.connectionSoftReference.get())) {
            this.closeChildren();
            if (this.connectionChildren.isEmpty()) {
                return true;
            } else {
                log.error("Child connection artifacts will not be reused " + this.connectionChildren.size());
                return false;
            }
        }
        else {
            return false;
        }
    }
    public Cleanup<?> getCleanup() {
        return this.cleanup;
    }

    public SQLException handleSQLException(SQLException sqlException, ConnectionArtifact_I delegatedInstance) {
        SqlExceptionWrapper sqlExceptionWrapper = this.exceptionAdvice.adviseSqlException(sqlException, delegatedInstance);
        this.reviewException(delegatedInstance, sqlExceptionWrapper.getRecommendation());
        return sqlExceptionWrapper;
    }

    public SQLClientInfoException handleSQLClientInfoException(SQLClientInfoException sqlClientInfoException, ConnectionArtifact_I delegatedInstance) {
        SqlClientInfoExceptionWrapper sqlClientInfoExceptionWrapper = this.exceptionAdvice.adviseSQLClientInfoException(sqlClientInfoException, delegatedInstance);
        this.reviewException(delegatedInstance, sqlClientInfoExceptionWrapper.getRecommendation());
        return sqlClientInfoExceptionWrapper;
    }

    public IOException handleIoException(IOException ioException, ConnectionArtifact_I delegatedInstance) {
        IoExceptionWrapper ioExceptionWrapper = this.exceptionAdvice.adviseIoException(ioException, delegatedInstance);
        this.reviewException(delegatedInstance, ioExceptionWrapper.getRecommendation());
        return ioExceptionWrapper;
    }

    public Exception handleException(Exception exception, ConnectionArtifact_I delegatedInstance) {
        ExceptionWrapper exceptionWrapper = this.exceptionAdvice.adviseException(exception, delegatedInstance);
        this.reviewException(delegatedInstance, exceptionWrapper.getRecommendation());
        return exceptionWrapper;
    }

    public ConnectionArtifact_I getConnection() {
        return this.connectionSoftReference.get();
    }

    public void closeChildren() {
        for (SoftReference<ConnectionArtifact_I> softReference: this.connectionChildren.keySet()) {
            ConnectionArtifact_I connectionArtifact = softReference.get();
            if (connectionArtifact != null) {
                DoDelegateClose(connectionArtifact.getDelegate());
            }
            softReference.clear();
        }
        this.connectionChildren.clear();
    }
    public void closeDelegate(ConnectionArtifact_I connectionArtifact) {
        boolean closeAll = this.getConnection().equals(connectionArtifact);
        for (SoftReference<ConnectionArtifact_I> softReference: this.connectionChildren.keySet()) {
            ConnectionArtifact_I refConnectionArtifact = softReference.get();
            if (refConnectionArtifact != null) {
                if (closeAll || connectionArtifact.equals(connectionArtifact)) {
                    DoDelegateClose(connectionArtifact.getDelegate());
                    softReference.clear();
                }
            }
        }
        try {
            if (connectionArtifact instanceof Connection) {
                ((Connection) connectionArtifact).endRequest();
            }
        }
        catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exception trying to endRequest() was ignored: ");
            PrintCause(e, stringBuilder);
            log.error(stringBuilder.toString());
        }
    }

    @SuppressWarnings("unchecked")
    public ConnectionArtifact_I wrap(Object delegate, Class delegateClassReturned, Object[] args) {
        ConnectionArtifact_I wrapped = connectionWrapper.getDelegate(this, delegate, delegateClassReturned, args);
        SoftReference<ConnectionArtifact_I> softReference = new SoftReference<>(wrapped, cleanup.globalReferenceQueue);
        if (this.connectionSoftReference == null && delegate instanceof Connection) {
            this.connectionSoftReference = softReference;
            this.currentState = State.Open;
            log.debug("added connection to artifact UUID=" + wrapped.getArtifactId() + " to container=" + containerId);
        }
        else {
            log.debug("Adding a child UUID=" + wrapped.getArtifactId() + " to container=" + containerId);
            this.connectionChildren.put(softReference,Instant.now());
        }
        return wrapped;
    }

    @Override
    public int compareTo(Container that) {
        if (this.containerId.equals(that.containerId)) {
            return 0;
        } else {
            return (this.createInstant.compareTo(that.createInstant) == 0) ?
                    this.containerId.compareTo(that.containerId) :
                    this.createInstant.compareTo(that.createInstant);
        }
    }

    private static void DoDelegateClose(Object delegate) {
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
    public static void PrintCause(Throwable t, StringBuilder stringBuilder) {
        stringBuilder.append(t.getMessage());
        if (t.getCause() != null) {
            stringBuilder.append("\n caused by: ");
            PrintCause(t.getCause(), stringBuilder);
        }
    }

    public void reviewException(ConnectionArtifact_I connectionArtifact, Recommendation recommendation) {
        if (recommendation.shouldClose() && connectionArtifact != null) {
            log.debug("Close delegate");
            this.closeDelegate(connectionArtifact);
        }
        if (recommendation.isFailoverToNextConnectionSupplier()) {
            log.info("Failover");
            this.cleanup.getFailoverConsumer().accept(false);
        }
    }

}
