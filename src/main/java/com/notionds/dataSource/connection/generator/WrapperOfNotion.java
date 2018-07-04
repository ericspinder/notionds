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

    public WrapperOfNotion(O options) {
        this.options = options;
    }


    public abstract Class<ConnectionMember_I> getDelegateClass(Class clazz);


}
