package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.ConnectionContainer;

public interface WrapperFactory_I {

    /**
     *
     * @param connectionContainer connection container
     * @param delegate the delegated class
     * @param delegateClassCreated this is the wrapped delegate
     * @return the wrapped delegate
     */
    <D> D getDelegate(ConnectionContainer connectionContainer, D delegate, Class<D> delegateClassCreated, Object... args);

}
