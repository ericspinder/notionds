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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionWrapperFactory<O extends Options> extends AbstractConnectionWrapperFactory<O> {

    private Logger logger = LogManager.getLogger(ConnectionWrapperFactory.class);

    public ConnectionWrapperFactory(O options) {
        super(options);
    }

    @Override
    public final ConnectionArtifact_I getDelegate(ConnectionContainer connectionContainer, Object delegate, Class delegateClassCreated, Object[] args) {
        logger.trace("getDelegate(...Object....");
        if (delegate instanceof InputStream) {
            return this.createInputStreamDelegate(connectionContainer, (InputStream) delegate, args);
        }
        else if (delegate instanceof OutputStream) {
            return this.createOutputStreamDelegate(connectionContainer, (OutputStream) delegate, args);
        }
        else if (delegate instanceof Reader) {
            return this.createReaderDelegate(connectionContainer, (Reader) delegate, args);
        }
        else if (delegateClassCreated.isInterface()) {
            Class[] interfaces = this.getConnectionMemberInterfaces(delegateClassCreated);
            if (interfaces != null) {
                return this.getProxyMember(interfaces, connectionContainer, delegate, args);
            }
            logger.error("No Interfaces, very odd as it's an interface: " + delegateClassCreated.getCanonicalName());
            throw new RuntimeException("No Interfaces, very odd as it's an interface: " + delegateClassCreated.getCanonicalName());
        }
        logger.error("ProxyDelegation is unable to create: " + delegateClassCreated.getCanonicalName());
        throw new RuntimeException("ProxyDelegation is unable to create: " + delegateClassCreated.getCanonicalName());
    }

    protected ConnectionArtifact_I getProxyMember(Class[] interfaces, ConnectionContainer connectionContainer, Object delegate, Object[] args) {
        ConnectionArtifact_I connectionMember = (ConnectionArtifact_I) Proxy.newProxyInstance(
                ConnectionWrapperFactory.class.getClassLoader(),
                interfaces,
                createProxyMember(connectionContainer, delegate, args));
        return connectionMember;
    }

    protected ConnectionArtifact_I createInputStreamDelegate(ConnectionContainer connectionContainer, InputStream delegate, Object[] args) {
        return new InputStreamConnectionArtifact(connectionContainer, delegate);
    }

    protected ConnectionArtifact_I createOutputStreamDelegate(ConnectionContainer connectionContainer, OutputStream delegate, Object[] args) {
        return new OutputStreamConnectionArtifact(connectionContainer, delegate);
    }

    protected ConnectionArtifact_I createReaderDelegate(ConnectionContainer connectionContainer, Reader delegate, Object[] args) {
        return new ReaderConnectionArtifact(connectionContainer, delegate);
    }

    protected ProxyConnectionArtifact createProxyMember(ConnectionContainer connectionContainer, Object delegate, Object[] args) {
        return new ProxyConnectionArtifact(connectionContainer, delegate);
    }

    protected Class[] getConnectionMemberInterfaces(Class clazz)  {
        if (interfacesCache.containsKey(clazz.getCanonicalName())) {
            return interfacesCache.get(clazz.getCanonicalName());
        }
        Class[] oldArray = clazz.getInterfaces();
        if (oldArray.length == 0) {
            return null;
        }
        Class[] newArray = new Class[oldArray.length + 1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        newArray[oldArray.length] = ConnectionArtifact_I.class;
        interfacesCache.put(clazz.getCanonicalName(), newArray);
        return newArray;
    }
    protected Map<String, Class[]> interfacesCache = new ConcurrentHashMap<>();
}
