package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;

public abstract class AbstractConnectionWrapperFactory<O extends Options> {

    protected final O options;

    public AbstractConnectionWrapperFactory(O options) {
        this.options = options;
    }


    /**
     *
     * @param connectionContainer
     * @param delegate
     * @param delegateClassCreated this is the class object which the containing may be moot for some delegate implementations
     * @return
     */
    public abstract <D> ConnectionArtifact_I getDelegate(ConnectionContainer<?,?,?,?> connectionContainer, D delegate, Class<D> delegateClassCreated, Object[] args);


}
