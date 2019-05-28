package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.proxyV1.InputStreamDelegate;
import com.notionds.dataSource.connection.delegation.proxyV1.OutputStreamDelegate;
import com.notionds.dataSource.connection.delegation.proxyV1.ReaderDelegate;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public abstract class DelegationOfNotion<O extends Options> {

    protected final O options;

    public DelegationOfNotion(O options) {
        this.options = options;
    }


    /**
     *
     * @param connectionContainer
     * @param delegate
     * @param delegateClassReturned this is the class object which the containing, if it is an interface the delegate may be 'safely' wrapped in a proxy, may be moot for some delegate implementations
     * @return
     */
    public abstract ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Object delegate, Class delegateClassReturned, Object[] args);


}
