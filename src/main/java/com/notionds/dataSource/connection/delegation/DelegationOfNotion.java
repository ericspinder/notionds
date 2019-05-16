package com.notionds.dataSource.connection.delegation;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.ConnectionContainer;

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
     * @param maybesql
     * @return
     */
    public abstract ConnectionMember_I getDelegate(ConnectionContainer connectionContainer, Object delegate, Class delegateClassReturned, String maybesql);

}
