package com.notionds.dataSource;

import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.cleanup.ConnectionCleanup;
import com.notionds.dataSource.connection.cleanup.GlobalCleanup;
import com.notionds.dataSource.connection.delegation.AbstractConnectionWrapperFactory;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.exceptions.ExceptionAdvice;
import com.notionds.dataSource.exceptions.SqlExceptionWrapper;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;

public abstract class NotionDs<O extends Options, EA extends ExceptionAdvice, P extends DatabasePool, W extends AbstractConnectionWrapperFactory<O>, CC extends ConnectionCleanup, GC extends GlobalCleanup<O, CC>> {

    private final O options;
    private final W delegation;
    private final EA exceptionAdvice;
    private final P databasePool;
    private final GC globalCleanup;
    private final ConnectionSupplier connectionSupplier;

    public interface ConnectionSupplier {
        Connection getConnection() throws SQLException;
    }

    public NotionDs(O options, W delegation, P databasePool, EA exceptionAdvice, GC globalCleanup, ConnectionSupplier connectionSupplier) {
        this.options = options;
        this.delegation = delegation;
        this.databasePool = databasePool;
        this.exceptionAdvice = exceptionAdvice;
        this.globalCleanup = globalCleanup;
        this.connectionSupplier = connectionSupplier;
    }

    public Connection acquireConnection() throws SqlExceptionWrapper {
        Connection connection = this.databasePool.getConnection();
        if (connection != null) {
            return connection;
        }
        try {
            Connection freshConnection = this.connectionSupplier.getConnection();

        }
        catch (SQLException sqle) {
            throw this.exceptionAdvice.adviseSqlException(sqle);
        }


    }

    protected <C extends Connection> Connection prepForDelivery(C connection, Instant expireInstant) {
        ConnectionContainer connectionContainer = new ConnectionContainer(this.options, this.exceptionAdvice, this.delegation, connection, this.globalCleanup);
        ConnectionArtifact_I connectionArtifact = connectionContainer.wrap(connection, Connection.class, null);
        globalCleanup.register(connectionArtifact, expireInstant);
        return connectionCleanup.getConnection(connectionContainer);
    }
    public Recommendation release(VC vendorConnection, Recommendation recommendation) {
        Recommendation finalRecommendation = this.databasePool.analyzeRelease(recommendation);
        if (finalRecommendation.equals(Recommendation.FailoverDatabase_Now)) {

        }
        return finalRecommendation;

    }
}
