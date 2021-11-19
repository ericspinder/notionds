package com.notionds.dataSupplier;

import com.notionds.dataSupplier.cron.Task;
import com.notionds.dataSupplier.delegation.Delegation;
import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.exceptions.Advice;
import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.cron.Cron;
import com.notionds.dataSupplier.pool.Pool;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.locks.StampedLock;

public class Controller<N, O extends Operational, W extends Wrapper<N>, S extends NotionSupplier<N,O,W,A,D,C>, A extends Advice<N,O,W>, D extends Delegation<N,O,W>, C extends Cron<N,O,W>, P extends Pool<N,O,W>> {

        private final Factory factory;
        protected final S notionSupplier;
        protected final O operational;
        protected final A advice;
        protected final D delegation;
        protected final P pool;
        protected final C cron;
        private final StampedLock memberGate = new StampedLock();

        public Controller(final O operational, final A advice, final D delegation, final C cron, final P pool, final S notionSupplier, final Factory factory) {
                this.operational = operational;
                this.advice = advice;
                this.delegation = delegation;
                this.pool = pool;
                this.cron = cron;
                this.notionSupplier = notionSupplier;
                this.factory = factory;
        }

        @SuppressWarnings("unchecked")
        public N getNotion(List<Task> tasks) {
                long readLock = memberGate.readLock();
                try {
                        W emptyWrapper = this.pool.loanPooledMember(() ->this.notionSupplier.getNewContainer(this));
                        cron.add(emptyWrapper,tasks).
                                emptyWrapper.getContainer().finalizeCheckoutFromPool();
                        return (N) wrapper;
                }
                finally {
                        memberGate.unlockRead(readLock);
                }
        }
        public W addNotion(N delegate, Duration duration) {
                long writeLock = memberGate.writeLock();
                try {
                        Container<N,O,W> newContainer = notionSupplier.getNewContainer(this);
                        cron.add(emptyWrapper, duration);

                }
                finally {
                        memberGate.unlockWrite(writeLock);
                }
        }


        public boolean returnToPool(W wrapper) {
                this.cron.removeFromCleanup(wrapper);
                return this.pool.addNotion(wrapper, true);
        }
        public W wrap(N delegate, Class<N> delegateClassReturned, Object[] args) {
                Container<N,O,W> container = notionSupplier.getNewContainer(this);
                W wrapped = delegation.getDelegate(container, delegate, delegateClassReturned, args);
                return wrapped;
        }
        public final Factory getFactory() {
                return this.factory;
        }
        public final O getOperational() {
                return this.operational;
        }

        public Collection<W> getDrain() {
                return this.drain;
        }

}
