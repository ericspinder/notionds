package com.notionds.dataSupplier.management;

import javax.management.MBeanOperationInfo;
import java.util.function.Supplier;

public class OperationMBeanWrapper<B> {

    private final Supplier<B> supplier;
    private final MBeanOperationInfo mBeanOperationInfo;

    public OperationMBeanWrapper(Supplier<B> supplier, MBeanOperationInfo mBeanOperationInfo) {
        this.supplier = supplier;
        this.mBeanOperationInfo = mBeanOperationInfo;
    }
}

