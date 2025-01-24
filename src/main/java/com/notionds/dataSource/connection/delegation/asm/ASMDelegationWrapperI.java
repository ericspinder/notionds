package com.notionds.dataSource.connection.delegation.asm;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.WrapperFactory_I;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

/**
 * This class is a starter for ASM Java Bytecode manipulation to create delegation
 * <a href="https://www.baeldung.com/java-asm">ASM tutorial</a>
 *
 */
public class ASMDelegationWrapperI implements WrapperFactory_I {

    private Map<Class<?>, Class<ConnectionArtifact_I<?>>> cache = new HashMap<>();
    private StampedLock creationGate = new StampedLock();

    public ASMDelegationWrapperI() {
    }

    @Override
    public <D> ConnectionArtifact_I<D> getDelegate(ConnectionContainer connectionContainer, D delegate, Class<D> delegateClassCreated, Object[] args) {
        Class<ConnectionArtifact_I<?>> delegateClass = cache.get(delegateClassCreated);
        if (delegateClass == null) {
            // The autostart should have captured all of the classes needed
        }
        return  null;
    }

    private void createDelegateObjectFromInterface(Class<?> delegateClass) {
        //ClassReader classReader = new ClassReader();
    }

}


