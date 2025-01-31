package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.ConnectionContainer;

import java.lang.ref.Cleaner;
import java.sql.Connection;

public interface ConnectionArtifact_I<D> {

    Cleaner CLEANER = Cleaner.create();

    InnerState<D> getInnerState();

    default ConnectionContainer getConnectionContainer() {
        return this.getInnerState().connectionContainer();
    }
    default D getDelegate() {
        return this.getInnerState().delegate();
    }


    record InnerState<D>(D delegate, ConnectionContainer connectionContainer) implements Runnable {
        @Override
            public void run() {
                if (delegate instanceof Connection) {
                    this.connectionContainer.getConnectionPool().returnConnection((Connection) delegate, connectionContainer, "Cleaner");
                }
            }
    }
}
