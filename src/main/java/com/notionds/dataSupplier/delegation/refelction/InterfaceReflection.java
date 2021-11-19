package com.notionds.dataSupplier.delegation.refelction;

import com.notionds.dataSupplier.Container;
import com.notionds.dataSupplier.NotionStartupException;
import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.delegation.Delegation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class InterfaceReflection<N,O extends Operational,W extends Wrapper<N> & InvocationHandler> extends Delegation<N,O,W> {

    private static Logger logger = LogManager.getLogger(InterfaceReflection.class);

    //public static final InterfaceReflection DEFAULT_INSTANCE = new InterfaceReflection<>(Operational.DEFAULT_OPTIONS_INSTANCE);

    public InterfaceReflection(O options, Supplier<W> proxySupplier) {
        super(options, proxySupplier);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final W getDelegate(Container<N,O,W> container, N delegate, Class<N> delegateClassCreated, Object[] args) {
        if (delegateClassCreated.isInterface()) {
            Class[] interfaces = this.getConnectionMemberInterfaces(delegateClassCreated);
            if (interfaces != null) {
                return (W) java.lang.reflect.Proxy.newProxyInstance(
                        InterfaceReflection.class.getClassLoader(),
                        interfaces,
                        this.proxySupplier.get());
//                        new Proxy<>(operational, container, delegate));
            }
            logger.error("No Interfaces, very odd as it's an interface: " + delegateClassCreated.getCanonicalName());
            throw new NotionStartupException(NotionStartupException.Type.ReflectiveOperationFailed, this.getClass());
        }
        logger.error("ProxyDelegation is unable to create: " + delegateClassCreated.getCanonicalName());
        throw new RuntimeException("ProxyDelegation is unable to create: " + delegateClassCreated.getCanonicalName());
    }
    protected Wrapper<InputStream> createInputStreamDelegate(Container<InputStream,?,?> container, InputStream delegate, Object[] args) {
        return new InputStreamMember(container, delegate);
    }

    protected Wrapper<OutputStream> createOutputStreamDelegate(Container<OutputStream,?,?> container, OutputStream delegate, Object[] args) {
        return new OutputStreamMember(container, delegate);
    }

    protected Wrapper<Reader> createReaderDelegate(Container<Reader,?,?> container, Reader delegate, Object[] args) {
        return new ReaderMember(container, delegate);
    }

    protected Class<?>[] getConnectionMemberInterfaces(Class<?> clazz)  {
        if (interfacesCache.containsKey(clazz.getCanonicalName())) {
            return interfacesCache.get(clazz.getCanonicalName());
        }
        List<Class<?>> classes = new ArrayList<>();
        classes.add(clazz);
        classes.addAll(Arrays.asList(clazz.getInterfaces()));
        classes.add(Wrapper.class);
        Class<?>[] classArray = classes.toArray( new Class[classes.size()]);
        interfacesCache.put(clazz.getCanonicalName(), classArray);
        return classArray;
    }
    protected Map<String, Class<?>[]> interfacesCache = new ConcurrentHashMap<>();
}
