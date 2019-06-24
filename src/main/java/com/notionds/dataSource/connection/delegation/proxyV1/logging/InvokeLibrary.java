package com.notionds.dataSource.connection.delegation.proxyV1.logging;

import com.notionds.dataSource.Options;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;

public abstract class InvokeLibrary<O extends Options, IA extends InvokeAccounting, IG extends InvokeAggerator<O, IA>> {

    public static class Default<O extends Options, IA extends InvokeAccounting, IG extends InvokeAggerator<O, IA>> extends InvokeLibrary<O, IA, IG> {

        public Default(O options) {
            super(options);
        }

        protected IG retrieveAggregator(String description) {
            return this.createAggregator(description);
        }
    }

    protected final O options;
    protected final Class<IA> invokeAccountingClass;
    protected final Class<IG> invokeAggregatorClass;
    protected final Constructor<IA> invokeAccountingConstructor;
    protected final Constructor<IG> invokeAggregatorConstructor;

    public InvokeLibrary(O options) {
        this.options = options;
        this.invokeAccountingClass = (Class<IA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.invokeAggregatorClass = (Class<IG>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[2];
        try {
            this.invokeAccountingConstructor = invokeAccountingClass.getDeclaredConstructor(UUID.class);
            this.invokeAggregatorConstructor = invokeAggregatorClass.getDeclaredConstructor(Options.class, String.class);
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("Problem creating Constructors", roe);
        }
    }
    protected abstract IG retrieveAggregator(String description);

    protected IG createAggregator(String description) {
        try {
            return this.invokeAggregatorConstructor.newInstance(description);
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("Problem creating InvokeAggregator instance (" + invokeAggregatorClass.getCanonicalName() + ") ", roe);
        }
    }
    public void populateAggregator(String description, IA invokeAccounting) {
        IG ig = this.retrieveAggregator(description);
        ig.addInvokeAccounting(invokeAccounting);
    }

    public IA createInvokeAccounting(UUID connectionId) {
        try {
            return this.invokeAccountingConstructor.newInstance(connectionId);
        }
        catch (ReflectiveOperationException roe) {
            throw new RuntimeException("Problem creating InvokeAggregator instance (" + invokeAccountingClass.getCanonicalName() + ") ", roe);
        }
    }

}
