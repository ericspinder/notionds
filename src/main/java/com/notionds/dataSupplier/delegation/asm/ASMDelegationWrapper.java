package com.notionds.dataSupplier.delegation.asm;

import com.notionds.dataSupplier.Container;
import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.delegation.Delegation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Supplier;

/**
 * This class is a starter for ASM Java Bytecode manipulation to create delegation
 * https://www.baeldung.com/java-asm
 *
 */
public class ASMDelegationWrapper<N,O extends Operational<N,W>,W extends Wrapper<N>> extends Delegation<N,O,W> {

    private Map<Class, Class<Wrapper>> cache = new HashMap<>();
    private StampedLock creationGate = new StampedLock();

    public ASMDelegationWrapper(O options, Supplier<W> wrapperSupplier) {
        super(options, wrapperSupplier);
    }

    @Override
    public W getDelegate(Container<N,O,W> container, N delegate, Class<N> delegateClassCreated, Object[] args) {
        Class<Wrapper> delegateClass = cache.get(delegateClassCreated);
        if (delegateClass == null) {
            // The autostart should have captured all of the classes needed
        }
        throw new UnsupportedOperationException("Stub class not completed");
    }

}


