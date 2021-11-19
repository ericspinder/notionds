package com.notionds.dataSupplier.delegation;

import com.notionds.dataSupplier.Container;
import com.notionds.dataSupplier.operational.Operational;

import java.util.function.Supplier;

public abstract class Delegation<N, O extends Operational, W extends Wrapper<N>> {

    protected final O operational;
    protected final Supplier<W> proxySupplier;

    public Delegation(O operational, Supplier<W> proxySupplier) {
        this.operational = operational;
        this.proxySupplier = proxySupplier;
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
