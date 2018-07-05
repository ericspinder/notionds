package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionMember_I;
import org.objectweb.asm.ClassReader;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

public class ASMWrapper<O extends Options> extends WrapperOfNotion {

    private Map<Class, Class<ConnectionMember_I>> cache = new HashMap<>();
    private StampedLock creationGate = new StampedLock();

    public ASMWrapper(O options) {
        super(options);
    }

    @Override
    public Class<ConnectionMember_I> getDelegateClass(Class clazz) {
        Class<ConnectionMember_I> delegateClass = cache.get(clazz);
        if (delegateClass != null) {
            return delegateClass;
        }
        // The autostart should have captured all of the classes needed
        return null;
    }

    private void createDelegateObjectFromInterface(Class delegateClass) {
        //ClassReader classReader = new ClassReader();
    }

}


