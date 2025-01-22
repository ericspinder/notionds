package com.notionds.dataSource.exceptions;

import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.sql.SQLException;

public class SqlExceptionWrapper extends SQLException implements NotionExceptionWrapper {

    private final Recommendation recommendation;
    private final ConnectionArtifact_I<?> connectionArtifact;

    public SqlExceptionWrapper(String message, SQLException cause, ConnectionArtifact_I<?> connectionArtifact, Recommendation recommendation) {
        super(message, cause);
        this.recommendation = recommendation;
        this.connectionArtifact = connectionArtifact;
    }
    /**
     * Use of 'this' as the return prevents a stack trace from being registered.
     * This is a wrapper and has nothing to do with the creation of the exception.
     * @return this
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public Recommendation getRecommendation() {
        return recommendation;
    }

    @Override
    public ConnectionArtifact_I<?> getConnectionArtifact() {
        return connectionArtifact;
    }
}
