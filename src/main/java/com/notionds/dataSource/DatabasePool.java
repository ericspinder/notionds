package com.notionds.dataSource;

import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.sql.Connection;

public abstract class DatabasePool<O extends Options> {

    protected final O options;

    public DatabasePool(O options) {
        this.options = options;
    }

    public static class Default<O extends Options> extends DatabasePool<O> {

        public Default(O options) {
            super(options);
        }

        @Override
        public <C extends Connection & ConnectionArtifact_I> C getConnection() {
            return null;
        }

        @Override
        public <C extends Connection & ConnectionArtifact_I> void returnConnection(C returned) {

        }


    }

    public abstract <C extends Connection & ConnectionArtifact_I> C getConnection();
    public abstract <C extends Connection & ConnectionArtifact_I> void returnConnection(C returned);
}
