package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.ConnectionContainer;

public interface WrapperFactory_I {

    /**
     *
     * @param connectionContainer
     * @param delegate
     * @param delegateClassCreated this is the wrapped delegate
     * @return
     */
    <D> ConnectionArtifact_I<D> getDelegate(ConnectionContainer connectionContainer, D delegate, Class<D> delegateClassCreated, Object... args);

}
