package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.ConnectionContainer;

import java.time.Instant;
import java.util.UUID;

public interface ConnectionArtifact_I<D> extends Comparable<ConnectionArtifact_I<?>> {

    UUID getArtifactId();

    ConnectionContainer getConnectionContainer();

    /**
     * This should always skip if the ConnectionContainer has been set already.
     */
    void setConnectionContainer(ConnectionContainer connectionContainer);

    D getDelegate();

    Instant getCreateInstant();

    default int compareTo(ConnectionArtifact_I<?> that) {
        if (this == that) {
            return 0;
        }
        if (that == null) {
            return -1;
        }
        int isZero = this.getCreateInstant().compareTo(((ConnectionArtifact_I<?>) that).getCreateInstant());
        if (isZero == 0) {
            return this.getArtifactId().compareTo(that.getArtifactId());
        }
        return isZero;
    }
}
