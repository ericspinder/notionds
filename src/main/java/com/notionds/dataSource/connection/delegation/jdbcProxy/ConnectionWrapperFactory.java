package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
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

    public static final ConnectionWrapperFactory<Options.Default> DEFAULT_INSTANCE = new ConnectionWrapperFactory<>(Options.DEFAULT_INSTANCE);

    public ConnectionWrapperFactory(O options) {
        super(options);
    }

    @Override
    public final <D> ConnectionArtifact_I getDelegate(ConnectionContainer<?,?,?,?> connectionContainer, D delegate, Class<D> delegateClassCreated, Object[] args) {
        logger.trace("getDelegate(...Object....");
        if (delegateClassCreated.isInterface()) {
            Class[] interfaces = this.getConnectionMemberInterfaces(delegateClassCreated);
            if (interfaces != null) {
                return this.getProxyMember(interfaces, connectionContainer, delegate, args);
            }
            logger.error("No Interfaces, very odd as it's an interface: " + delegateClassCreated.getCanonicalName());
            throw new RuntimeException("No Interfaces, very odd as it's an interface: " + delegateClassCreated.getCanonicalName());
        }
        else if (delegate instanceof InputStream) {
            return this.createInputStreamDelegate(connectionContainer, (InputStream) delegate, args);
        }
        else if (delegate instanceof OutputStream) {
            return this.createOutputStreamDelegate(connectionContainer, (OutputStream) delegate, args);
        }
        else if (delegate instanceof Reader) {
            return this.createReaderDelegate(connectionContainer, (Reader) delegate, args);
        }
        logger.error("ProxyDelegation is unable to create: " + delegateClassCreated.getCanonicalName());
        throw new RuntimeException("ProxyDelegation is unable to create: " + delegateClassCreated.getCanonicalName());
    }

    protected <D> ConnectionArtifact_I getProxyMember(Class<?>[] interfaces, ConnectionContainer<?,?,?,?> connectionContainer, D delegate, Object[] args) {
        return (ConnectionArtifact_I) Proxy.newProxyInstance(
                ConnectionWrapperFactory.class.getClassLoader(),
                interfaces,
                createProxyMember(connectionContainer, delegate, null));
    }

    protected ConnectionArtifact_I createInputStreamDelegate(ConnectionContainer<?,?,?,?> connectionContainer, InputStream delegate, Object[] args) {
        return new InputStreamConnectionArtifact(connectionContainer, delegate);
    }

    protected ConnectionArtifact_I createOutputStreamDelegate(ConnectionContainer<?,?,?,?> connectionContainer, OutputStream delegate, Object[] args) {
        return new OutputStreamConnectionArtifact(connectionContainer, delegate);
    }

    protected ConnectionArtifact_I createReaderDelegate(ConnectionContainer<?,?,?,?> connectionContainer, Reader delegate, Object[] args) {
        return new ReaderConnectionArtifact(connectionContainer, delegate);
    }

    protected <D> ProxyConnectionArtifact<D> createProxyMember(ConnectionContainer<?,?,?,?> connectionContainer, D delegate, Object[] args) {
        return new ProxyConnectionArtifact<D>(connectionContainer, delegate);
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
