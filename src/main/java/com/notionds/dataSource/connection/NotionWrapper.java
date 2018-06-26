package com.notionds.dataSource.connection;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.delegate.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.UUID;

public abstract class NotionWrapper<O extends Options, NC extends NotionConnection,  DT extends DelegateTree> {

    private final Class<NC> notionConnectionClass = ((Class<NC>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);

    private WeakReference<NC> notionConnection = null;
    private final O options;
    private final UUID connectionId = UUID.randomUUID();
    private final VendorConnection vendorConnection;

    public NotionWrapper(O options, VendorConnection vendorConnection) {
        this.options = options;
        this.vendorConnection = vendorConnection;
    }
    public NC getNotionConnection() {
        if (this.notionConnection == null) {
            try {
                this.notionConnection = new WeakReference<>(notionConnectionClass.getDeclaredConstructor(getClass()).newInstance(this));
            }
            catch(Exception e) {
                throw new RuntimeException("Problem creating NotionConnection instance " + e.getMessage());
            }
        }
        return this.notionConnection.get();
    }
    public Connection getUnderlyingVendorConnection() {
        return this.vendorConnection.getDelegate();
    }
    public final UUID getConnectionId() {
        return this.connectionId;
    }

    public abstract void close(ConnectionMember_I delegatedInstance);

    protected abstract void setNotionConnection(NotionConnectionDelegate notionConnection);


}
