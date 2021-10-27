package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.Container;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import com.notionds.dataSource.connection.delegation.AbstractConnectionWrapperFactory;
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

public class ConnectionWrapperFactory<O extends Options> extends AbstractConnectionWrapperFactory<O> {

    private static Logger logger = LogManager.getLogger(ConnectionWrapperFactory.class);

    public static final ConnectionWrapperFactory<Options.Default> DEFAULT_INSTANCE = new ConnectionWrapperFactory<>(Options.DEFAULT_OPTIONS_INSTANCE);

    public ConnectionWrapperFactory(O options) {
        super(options);
    }

    @Override
    public final <D> ConnectionArtifact_I getDelegate(Container<?,?,?> container, D delegate, Class<D> delegateClassCreated, Object[] args) {
        logger.debug("getDelegate(...Object....");
        if (delegateClassCreated.isInterface()) {
            Class[] interfaces = this.getConnectionMemberInterfaces(delegateClassCreated);
            if (interfaces != null) {
                return this.getProxyMember(interfaces, container, delegate, args);
            }
            logger.error("No Interfaces, very odd as it's an interface: " + delegateClassCreated.getCanonicalName());
            throw new RuntimeException("No Interfaces, very odd as it's an interface: " + delegateClassCreated.getCanonicalName());
        }
        else if (delegate instanceof InputStream) {
            return this.createInputStreamDelegate(container, (InputStream) delegate, args);
        }
        else if (delegate instanceof OutputStream) {
            return this.createOutputStreamDelegate(container, (OutputStream) delegate, args);
        }
        else if (delegate instanceof Reader) {
            return this.createReaderDelegate(container, (Reader) delegate, args);
        }
        logger.error("ProxyDelegation is unable to create: " + delegateClassCreated.getCanonicalName());
        throw new RuntimeException("ProxyDelegation is unable to create: " + delegateClassCreated.getCanonicalName());
    }

    protected <D> ConnectionArtifact_I getProxyMember(Class<?>[] interfaces, Container<?,?,?> container, D delegate, Object[] args) {
        return (ConnectionArtifact_I) Proxy.newProxyInstance(
                ConnectionWrapperFactory.class.getClassLoader(),
                interfaces,
                createProxyMember(container, delegate, args));
    }

    protected ConnectionArtifact_I createInputStreamDelegate(Container<?,?,?> container, InputStream delegate, Object[] args) {
        return new InputStreamConnectionArtifact(container, delegate);
    }

    protected ConnectionArtifact_I createOutputStreamDelegate(Container<?,?,?> container, OutputStream delegate, Object[] args) {
        return new OutputStreamConnectionArtifact(container, delegate);
    }

    protected ConnectionArtifact_I createReaderDelegate(Container<?,?,?> container, Reader delegate, Object[] args) {
        return new ReaderConnectionArtifact(container, delegate);
    }

    protected <D> ProxyConnectionArtifact<D> createProxyMember(Container<?,?,?> container, D delegate, Object[] args) {
        return new ProxyConnectionArtifact<D>(container, delegate);
    }

    protected Class<?>[] getConnectionMemberInterfaces(Class<?> clazz)  {
        if (interfacesCache.containsKey(clazz.getCanonicalName())) {
            return interfacesCache.get(clazz.getCanonicalName());
        }
        List<Class<?>> classes = new ArrayList<>();
        classes.add(clazz);
        classes.addAll(Arrays.stream(clazz.getInterfaces()).toList());
        classes.add(ConnectionArtifact_I.class);
        Class<?>[] classArray = classes.toArray( new Class[classes.size()]);
        interfacesCache.put(clazz.getCanonicalName(), classArray);
        return classArray;
    }
    protected Map<String, Class<?>[]> interfacesCache = new ConcurrentHashMap<>();
}
