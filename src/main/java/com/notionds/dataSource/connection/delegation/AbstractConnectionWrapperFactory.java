package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.Container;

public abstract class AbstractConnectionWrapperFactory<O extends Options> {

    protected final O options;

    public AbstractConnectionWrapperFactory(O options) {
        this.options = options;
    }


    /**
     *
     * @param container
     * @param delegate
     * @param delegateClassCreated this is the wrapped delegate
     * @return
     */
    public abstract <D> ConnectionArtifact_I getDelegate(Container<?,?,?> container, D delegate, Class<D> delegateClassCreated, Object[] args);


}
