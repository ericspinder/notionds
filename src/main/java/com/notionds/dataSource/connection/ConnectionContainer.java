package com.notionds.dataSource.connection;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.ConnectionAction;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.AbstractConnectionWrapperFactory;
import com.notionds.dataSource.exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.sql.Connection;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConnectionContainer<O extends Options,
        A extends Advice,
        W extends AbstractConnectionWrapperFactory,
        C extends Cleanup> implements Comparable<ConnectionContainer> {

    private static final Logger logger = LogManager.getLogger(ConnectionContainer.class);

    private final O options;
    public final UUID connectionId = UUID.randomUUID();
    public final Instant createInstant = Instant.now();
    private Instant lastCheck;
    private final A exceptionAdvice;
    private final W connectionWrapper;
    private final C cleanup;
    public volatile State currentState;
    private SoftReference<ConnectionArtifact_I> connectionSoftReference;
    private Map<SoftReference<ConnectionArtifact_I>, Instant> connectionChildren = new HashMap<>();

    public ConnectionContainer(O options, A exceptionAdvice, W connectionWrapper, C cleanup) {
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
        this.lastCheck = Instant.now();
        return (Connection) this.connectionSoftReference.get();
    }
    public boolean reuse(ConnectionArtifact_I artifact) {
        if (this.currentState.equals(State.Open) && artifact.equals(this.connectionSoftReference.get())) {
            if (this.connectionChildren.isEmpty()) {
                this.lastCheck = Instant.now();
                this.closeChildren();
                this.cleanup.timeoutCleanup.remove(this);
                return true;
            } else {
                logger.error("ConnectionChildren WeakHashMap size=" + this.connectionChildren.size());
                return false;
            }
        }
        else {
            return false;
        }
    }
    public C getCleanup() {
        return this.cleanup;
    }

    public SQLException handleSQLException(SQLException sqlException, ConnectionArtifact_I delegatedInstance) {
        SqlExceptionWrapper sqlExceptionWrapper = this.exceptionAdvice.adviseSqlException(sqlException);
        this.reviewException(delegatedInstance, sqlExceptionWrapper.getRecommendation());
        return sqlExceptionWrapper;
    }

    public SQLClientInfoException handleSQLClientInfoException(SQLClientInfoException sqlClientInfoException, ConnectionArtifact_I delegatedInstance) {
        SqlClientInfoExceptionWrapper sqlClientInfoExceptionWrapper = this.exceptionAdvice.adviseSQLClientInfoException(sqlClientInfoException);
        this.reviewException(delegatedInstance, sqlClientInfoExceptionWrapper.getRecommendation());
        return sqlClientInfoExceptionWrapper;
    }

    public IOException handleIoException(IOException ioException, ConnectionArtifact_I delegatedInstance) {
        IoExceptionWrapper ioExceptionWrapper = this.exceptionAdvice.adviseIoException(ioException);
        this.reviewException(delegatedInstance, ioExceptionWrapper.getRecommendation());
        return ioExceptionWrapper;
    }

    public Exception handleException(Exception exception, ConnectionArtifact_I delegatedInstance) {
        ExceptionWrapper exceptionWrapper = this.exceptionAdvice.adviseException(exception);
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
                connectionArtifact.closeDelegate();
            }
            softReference.clear();
        }
        this.connectionChildren.clear();
    }
    public void closeChild(ConnectionArtifact_I connectionArtifact) {
        System.out.println(connectionArtifact.getUuid() + " class=" + connectionArtifact.getClass().getCanonicalName());
        for (SoftReference<ConnectionArtifact_I> softReference: this.connectionChildren.keySet()) {
            ConnectionArtifact_I suspectConnectionArtifact = softReference.get();
            if (suspectConnectionArtifact != null) {
                System.out.println(suspectConnectionArtifact.getUuid() + " class=" + suspectConnectionArtifact.getClass().getCanonicalName());
                if (suspectConnectionArtifact.equals(connectionArtifact)) {
                    connectionArtifact.closeDelegate();
                    softReference.clear();
                    System.out.println("cleared");
                }
                else {
                    System.out.println("not cleared");
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public ConnectionArtifact_I wrap(Object delegate, Class delegateClassReturned, Object[] args) {
        ConnectionArtifact_I wrapped = connectionWrapper.getDelegate(this, delegate, delegateClassReturned, args);
        SoftReference<ConnectionArtifact_I> softReference = new SoftReference<>(wrapped, cleanup.globalReferenceQueue);
        if (this.connectionSoftReference == null && delegate instanceof Connection) {
            this.connectionSoftReference = softReference;
            this.currentState = State.Open;
            //System.out.println("added connection to container UUID=" + wrapped.getUuid());
        }
        else {
            //System.out.println("Adding a child UUID=" + wrapped.getUuid());
            this.connectionChildren.put(softReference,Instant.now());
        }
        return wrapped;
    }

    @Override
    public int compareTo(ConnectionContainer that) {
        if (this.connectionId.equals(that.connectionId)) {
            return 0;
        } else {
            return (this.createInstant.compareTo(that.createInstant) == 0) ?
                    this.connectionId.compareTo(that.connectionId) :
                    this.createInstant.compareTo(that.createInstant);
        }
    }

    public void reviewException(ConnectionArtifact_I connectionArtifact, ConnectionAction connectionAction) {
        if (connectionAction.shouldClose()) {
            connectionArtifact.closeDelegate();
        }
    }
}
