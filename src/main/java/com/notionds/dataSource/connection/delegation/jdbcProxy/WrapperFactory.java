package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.NotionStartupException;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.WrapperFactory_I;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WrapperFactory implements WrapperFactory_I {

    private static final Logger logger = LogManager.getLogger(WrapperFactory.class);

    public WrapperFactory() {}

    @Override
    @SuppressWarnings("unchecked")
    public <D> D getDelegate(ConnectionContainer connectionContainer, D delegate, Class<D> delegateClassCreated, Object... args) {
        if (delegateClassCreated.isInterface()) {
            Class<D>[] interfaces = (Class<D>[]) this.getConnectionMemberInterfaces(delegateClassCreated);
            if (interfaces != null) {
                return this.getProxyMember(interfaces, connectionContainer, delegate, args);
            }
            logger.error("No Interfaces, very odd as it's an interface: " + delegateClassCreated.getCanonicalName());
            throw new NotionStartupException(NotionStartupException.Type.ReflectiveOperationFailed, this.getClass());
        }
        else if (delegate instanceof InputStream) {
            return (D) this.createInputStreamDelegate(connectionContainer, (InputStream) delegate, args);
        }
        else if (delegate instanceof OutputStream) {
            return (D) this.createOutputStreamDelegate(connectionContainer, (OutputStream) delegate, args);
        }
        else if (delegate instanceof Reader) {
            return (D) this.createReaderDelegate(connectionContainer, (Reader) delegate, args);
        }
        logger.error("ProxyDelegation is unable to create: " + delegateClassCreated.getCanonicalName());
        throw new RuntimeException("ProxyDelegation is unable to create: " + delegateClassCreated.getCanonicalName());
    }

    @SuppressWarnings("unchecked")
    protected <D> D getProxyMember(Class<?>[] interfaces, ConnectionContainer connectionContainer, D delegate, Object[] args) {
        return (D) Proxy.newProxyInstance(WrapperFactory.class.getClassLoader(), interfaces, new ProxyConnectionArtifact<>(connectionContainer,delegate));
    }

    protected InputStream createInputStreamDelegate(ConnectionContainer connectionContainer, InputStream delegate, Object[] args) {
        return new InputStreamConnectionArtifact(delegate, connectionContainer);
    }

    protected OutputStream createOutputStreamDelegate(ConnectionContainer connectionContainer, OutputStream delegate, Object[] args) {
        return new OutputStreamConnectionArtifact(delegate, connectionContainer);
    }

    protected Reader createReaderDelegate(ConnectionContainer connectionContainer, Reader delegate, Object[] args) {
        return new ReaderConnectionArtifact(delegate, connectionContainer);
    }

    protected Class<?>[] getConnectionMemberInterfaces(Class<?> clazz)  {
        return getClasses(clazz, interfacesCache);
    }

    static Class<?>[] getClasses(Class<?> clazz, Map<String, Class<?>[]> interfacesCache) {
        if (interfacesCache.containsKey(clazz.getCanonicalName())) {
            return interfacesCache.get(clazz.getCanonicalName());
        }
        List<Class<?>> classes = new ArrayList<>();
        classes.add(clazz);
        classes.addAll(Arrays.stream(clazz.getInterfaces()).toList());
        classes.add(ConnectionArtifact_I.class);
        Class<?>[] classArray = classes.toArray(new Class[0]);
        interfacesCache.put(clazz.getCanonicalName(), classArray);
        return classArray;
    }

    protected Map<String, Class<?>[]> interfacesCache = new ConcurrentHashMap<>();
}
