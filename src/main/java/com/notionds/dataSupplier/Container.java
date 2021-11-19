package com.notionds.dataSupplier;

import com.notionds.dataSupplier.cron.Receipt;
import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.pool.Situation;
import com.notionds.dataSupplier.exceptions.ExceptionWrapper;
import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.exceptions.Recommendation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.sql.*;
import java.time.Instant;
import java.util.UUID;

public abstract class Container<N, O extends Operational<N,W>, W extends Wrapper<N>> {
    protected final static Logger logger = LogManager.getLogger();
    public final UUID containerId = UUID.randomUUID();
    public final Instant createInstant = Instant.now();
    protected final O options;
    protected final Controller<N,O, W,?,?,?,?,?> controller;
    public volatile Situation currentSituation;
    private Receipt<N,O,W> receipt = null;

    public Container(O options, Controller<N,O, W,?,?,?,?,?> controller) {
        this.options = options;
        this.controller = controller;
        this.currentSituation = Situation.New_Unattached;
    }

    /**
     *
     * @return true to continue false to reject use of object
     */
    protected abstract boolean checkout();
    public boolean checkout(Receipt<N,O,W> receipt) {
        this.receipt = receipt;
        return this.checkout();
    }
    protected abstract boolean reuseInterest(boolean currently);

    public boolean reuseInterest() {
        return this.reuseInterest(this.currentSituation.equals(Situation.Open));
    }

    public void handleException(Exception exception, Wrapper delegatedInstance) {
        logger.debug(exception.getMessage());
        ExceptionWrapper exceptionWrapper = this.controller.advice.adviseException(exception);
        this.reviewException(delegatedInstance, exceptionWrapper.getRecommendation());
    }

    public void closeDelegate() {
        this.currentSituation = Situation.Closed;
        DoDelegateClose(this.getDelegate());
        if (this.receipt != null) {
            this.receipt.clear();
        }
    }

    public static void DoDelegateClose(Object delegate) {
        try {
            if (delegate instanceof Closeable) {
                ((Closeable) delegate).close();
            } else if (delegate instanceof Clob) {
                ((Clob) delegate).free();
            } else if (delegate instanceof Blob) {
                ((Blob) delegate).free();
            } else if (delegate instanceof Array) {
                ((Array) delegate).free();
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exception trying to clean up Reference was ignored: ");
            Container.PrintCause(e, stringBuilder);
            logger.error(stringBuilder.toString());
        }
    }

    public static void PrintCause(Throwable t, StringBuilder stringBuilder) {
        stringBuilder.append(t.getMessage());
        if (t.getCause() != null) {
            stringBuilder.append("\n caused by: ");
            Container.PrintCause(t.getCause(), stringBuilder);
        }
    }

    public Receipt<N,O,W> getReceipt() {
        return this.receipt;
    }
    public Controller<N,O, W,?,?,?,?,?> getBridge() {
        return this.controller;
    }

    public abstract void reviewException(Wrapper wrapperArtifact, Recommendation recommendation);
}
