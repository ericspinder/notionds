package com.notionds.dataSupplier.delegation.reflection;

import com.notionds.dataSupplier.Container;
import com.notionds.dataSupplier.NotionStartupException;
import com.notionds.dataSupplier.delegation.Delegation;
import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.operational.Operational;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

public class WrappedDelegation<N, O extends Operational<N,W>, W extends Wrapper<N>,T extends Container<N,O,W>> extends Delegation<N,O,W> {

    private Constructor<W> wrapperConstructor = null;
    @SuppressWarnings("unchecked")
    public WrappedDelegation(O operational, ClassLoader classLoader) {
        super(operational, classLoader);
        Class<N> delegateClass = ((Class<N>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        Class<O> operationalClass = ((Class<O>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
        Class<T> containerClass = ((Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[3]);
        try {
            this.wrapperConstructor =  ((Class<W>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[2])
                    .getConstructor(operationalClass, containerClass, delegateClass);
        }
        catch (NoSuchMethodException e) {
            throw new NotionStartupException(NotionStartupException.Type.ConstructorProblem, this.getClass());
        }
    }

    @Override
    public W getDelegate(Container<N, O, W> container, N delegate, Class<N> delegateClassCreated, Object[] args) {
        try {
            return this.wrapperConstructor.newInstance(this.operational, container, delegate);
        }
        catch (ReflectiveOperationException e) {
            throw new NotionStartupException(NotionStartupException.Type.ConstructorProblem, this.getClass());
        }
    }
}
