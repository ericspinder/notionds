package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.NotionWeakReference;
import com.notionds.dataSource.connection.manual9.*;

import java.io.InputStream;
import java.io.Reader;
import java.sql.*;

public abstract class WrapperOfNotion<O extends Options> {

    protected final O options;
    protected final ConnectionContainer connectionContainer;

    public WrapperOfNotion(O options, ConnectionContainer connectionContainer) {
        this.options = options;
        this.connectionContainer = connectionContainer;
    }


    public abstract ConnectionMember<?, Statement> createDelegate(Object object);



}
