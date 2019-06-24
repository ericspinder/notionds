package com.notionds.dataSource.connection.delegation.asm;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionMember;
import com.notionds.dataSource.connection.delegation.DelegationOfNotion;
import com.notionds.dataSource.connection.delegation.proxyV1.logging.Logging_forDbObject;

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
    public ConnectionMember getDelegate(ConnectionContainer connectionContainer, Object delegate, Class clazz, Object[] args) {
        Class<ConnectionMember> delegateClass = cache.get(clazz);
        if (delegateClass == null) {
            // The autostart should have captured all of the classes needed
        }
        return  null;
    }

    private void createDelegateObjectFromInterface(Class delegateClass) {
        //ClassReader classReader = new ClassReader();
    }

}


