package com.notionds.dataSource.connection.generator;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionMember_I;

public abstract class WrapperOfNotion<O extends Options> {

    protected final O options;

    public WrapperOfNotion(O options) {
        this.options = options;
    }


    public abstract ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Object delegate, Class clazz);


}
