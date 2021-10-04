package com.notionds.dataSource.connection;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.Recommendation;
import com.notionds.dataSource.connection.cleanup.GlobalCleanup;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.AbstractConnectionWrapperFactory;
import com.notionds.dataSource.exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import java.util.WeakHashMap;

public class ConnectionContainer<O extends Options,
        EA extends ExceptionAdvice,
        W extends AbstractConnectionWrapperFactory,
        GC extends GlobalCleanup> implements Comparable<ConnectionContainer> {

    private static final Logger logger = LogManager.getLogger(ConnectionContainer.class);

    private final O options;
    private final UUID connectionId = UUID.randomUUID();
    private final Instant createInstant = Instant.now();
    private final EA exceptionAdvice;
    private final W connectionWrapper;
    private final GC globalCleanup;
    public volatile State currentState;
    private WeakReference<ConnectionArtifact_I> connectionWeakReference;
    private WeakHashMap<WeakReference<ConnectionArtifact_I>, Instant> connectionChildren;

    public ConnectionContainer(O options, EA exceptionAdvice, W connectionWrapper, GC globalCleanup) {
        this.options = options;
        this.exceptionAdvice = exceptionAdvice;
        this.connectionWrapper = connectionWrapper;
        this.globalCleanup = globalCleanup;
        this.currentState = State.New;
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

    public final UUID getConnectionId() {
        return this.connectionId;
    }

    public ConnectionArtifact_I getConnection() {
        return this.connectionWeakReference.get();
    }

    public void updateConnectionExpire(Instant expireInstant) {
        if (expireInstant == null) {
            this.globalCleanup.timeoutCleanup.remove(this);
        } else {
            this.globalCleanup.timeoutCleanup.put(this, expireInstant);
        }
    }

    public void closeChildren() {
        for (ConnectionArtifact_I connectionArtifact: this.connectionChildren.keySet()) {
            this.close(connectionArtifact);
        }
    }

    public ConnectionArtifact_I wrap(Object delegate, Class delegateClassReturned, Object[] args) {
        ConnectionArtifact_I wrapped = connectionWrapper.getDelegate(this, delegate, delegateClassReturned, args);
        WeakReference<ConnectionArtifact_I> weakReference = new WeakReference<>(wrapped, globalCleanup.globalReferenceQueue);
        if (this.connectionWeakReference == null && wrapped instanceof Connection) {
            this.connectionWeakReference = weakReference;
            this.currentState = State.Open;
        }
        return weakReference.get();
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

    public void reviewException(ConnectionArtifact_I connectionArtifact, Recommendation recommendation) {
        if (recommendation.shouldClose()) {
            this.close(connectionArtifact);
        }
        else {

        }
    }
    protected void close(ConnectionArtifact_I connectionArtifact) {
        try {
            connectionArtifact.closeDelegate();
        } catch (Exception e) {
            logger.error("Error closing delegate" + e);
        } finally {
            this.currentState = State.Empty;
        }
    }
}
