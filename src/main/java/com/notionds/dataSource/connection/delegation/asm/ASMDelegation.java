package com.notionds.dataSource.connection.delegation.asm;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionMain;
import com.notionds.dataSource.connection.delegation.ConnectionMember;
import com.notionds.dataSource.connection.delegation.DelegationOfNotion;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

/**
 * This class is a starter for ASM Java Bytecode manipulation to create delegation
 * https://www.baeldung.com/java-asm
 *
 */
public class ASMDelegation<O extends Options> extends DelegationOfNotion<O> {

    private Map<Class, Class<ConnectionMember>> cache = new HashMap<>();
    private StampedLock creationGate = new StampedLock();

    public ASMDelegation(O options) {
        super(options);
    }

    @Override
    public ConnectionMember getDelegate(ConnectionMain connectionMain, Object delegate, Class clazz, Object[] args) {
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


