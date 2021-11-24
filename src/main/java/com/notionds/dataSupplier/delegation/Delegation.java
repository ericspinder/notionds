package com.notionds.dataSupplier.delegation;

import com.notionds.dataSupplier.Container;
import com.notionds.dataSupplier.operational.Operational;

public abstract class Delegation<N, O extends Operational<N,W>, W extends Wrapper<N>> {

    protected final O operational;
    protected final ClassLoader classLoader;

    public Delegation(O operational, ClassLoader classLoader) {
        this.operational = operational;
        this.classLoader = classLoader;
    }


    /**
     *
     * @param container
     * @param delegate
     * @param delegateClassCreated this is the wrapped delegate
     * @return
     */
    public abstract W getDelegate(Container<N,O,W> container, N delegate, Class<N> delegateClassCreated, Object[] args);


}
