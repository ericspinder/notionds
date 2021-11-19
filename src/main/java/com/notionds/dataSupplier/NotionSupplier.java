package com.notionds.dataSupplier;

import com.notionds.dataSupplier.delegation.Delegation;
import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.exceptions.Advice;
import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.cron.Cron;

public abstract class NotionSupplier<N, O extends Operational, W extends Wrapper<N>, A extends Advice<N,O,W>, D extends Delegation<N,O,W>, C extends Cron<N,O,W>> {

    private final O options;
    private final String name;

    protected NotionSupplier(O options, String name) {
        this.options = options;
        this.name = name;
    }


    public abstract Container<N,O,W> getNewContainer(Controller<N,O,W,?,A,D,C,?> controller);
    public abstract N createNotion();
}
