package com.notionds.dataSupplier.delegation;

import com.notionds.dataSupplier.Container;

import java.util.UUID;

public interface Wrapper<N> {

    UUID getArtifactId();

    Container<N,?,?> getContainer();

    N getDelegate();

}
