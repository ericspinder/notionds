package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

public class ASMDelegation<O extends Options> extends DelegationOfNotion<O> {

    private Map<Class, Class<ConnectionMember>> cache = new HashMap<>();
    private StampedLock creationGate = new StampedLock();

    public ASMDelegation(O options) {
        super(options);
    }

    @Override
    public ConnectionMember getDelegate(ConnectionContainer connectionContainer, Object delegate, Class clazz) {
        Class<ConnectionMember> delegateClass = cache.get(clazz);
        if (delegateClass == null) {
            // The autostart should have captured all of the classes needed
        }
        try {
            return delegateClass.getDeclaredConstructor(ConnectionContainer.class, Object.class).newInstance(connectionContainer, delegate);
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("Problem creating new delegated class using the ASMDelegation" + roe.getMessage(), roe);
        }
    }

    private void createDelegateObjectFromInterface(Class delegateClass) {
        //ClassReader classReader = new ClassReader();
    }

}


