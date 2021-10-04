package com.notionds.dataSource.connection.delegation.asm;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.AbstractConnectionWrapperFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

/**
 * This class is a starter for ASM Java Bytecode manipulation to create delegation
 * https://www.baeldung.com/java-asm
 *
 */
public class ASMDelegationWrapper<O extends Options> extends AbstractConnectionWrapperFactory<O> {

    private Map<Class, Class<ConnectionArtifact_I>> cache = new HashMap<>();
    private StampedLock creationGate = new StampedLock();

    public ASMDelegationWrapper(O options) {
        super(options);
    }

    @Override
    public ConnectionArtifact_I getDelegate(ConnectionContainer connectionContainer, Object delegate, Class delegateClassCreated, Object[] args) {
        Class<ConnectionArtifact_I> delegateClass = cache.get(delegateClassCreated);
        if (delegateClass == null) {
            // The autostart should have captured all of the classes needed
        }
        return  null;
    }

    private void createDelegateObjectFromInterface(Class delegateClass) {
        //ClassReader classReader = new ClassReader();
    }

}


