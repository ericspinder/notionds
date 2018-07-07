package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionMember_I;
import org.objectweb.asm.ClassReader;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

public class ASMWrapper<O extends Options> extends WrapperOfNotion<O> {

    private Map<Class, Class<ConnectionMember>> cache = new HashMap<>();
    private StampedLock creationGate = new StampedLock();

    public ASMWrapper(O options) {
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
            throw new RuntimeException("Problem creating new delegated class using the ASMWrapper" + roe.getMessage(), roe);
        }
    }

    private void createDelegateObjectFromInterface(Class delegateClass) {
        //ClassReader classReader = new ClassReader();
    }

}


