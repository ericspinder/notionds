package com.notionds.dataSource;

import com.notionds.dataSource.connection.ConnectionMain;
import com.notionds.dataSource.connection.VendorConnection;
import com.notionds.dataSource.connection.cleanup.ConnectionCleanup;
import com.notionds.dataSource.connection.cleanup.GlobalCleanup;
import com.notionds.dataSource.connection.delegation.DelegationOfNotion;
import com.notionds.dataSource.exceptions.ExceptionAdvice;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.time.Instant;
import java.util.concurrent.locks.StampedLock;

public abstract class VendorMain<O extends Options, EA extends ExceptionAdvice, DA extends DatabaseAnalysis, D extends DelegationOfNotion<O>, VC extends VendorConnection<O>, CC extends ConnectionCleanup, GC extends GlobalCleanup<O, CC, VC>> {

    private final O options;
    private final Class<VC> vendorConnectionClass = ((Class<VC>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[4]);
    private final D delegation;
    private final EA exceptionAdvice;
    private final DA databaseAnalysis;
    private final GC globalCleanup;
    private final Constructor<VC> vcConstructor;
    private final String friendlyName;
    private final StampedLock connectionLock = new StampedLock();

    public VendorMain(O options, String friendlyName, D delegation, DA databaseAnalysis, EA exceptionAdvice, GC globalCleanup) {
        this.options = options;
        this.friendlyName = friendlyName;
        this.delegation = delegation;
        this.databaseAnalysis = databaseAnalysis;
        this.exceptionAdvice = exceptionAdvice;
        this.globalCleanup = globalCleanup;
        try {
            this.vcConstructor = this.vendorConnectionClass.getDeclaredConstructor(options.getClass());
        }
        catch (Exception e) {
            throw new RuntimeException("Problem getting vendor connection constructor " + e.getMessage());
        }

    }
    protected abstract VC createVendorConnection();

    protected Connection createConnection(VC vendorConnection, Instant expireInstant) {
        CC connectionCleanup = this.globalCleanup.register(vendorConnection, expireInstant);
        ConnectionMain connectionMain = new ConnectionMain(this.options, this.exceptionAdvice, this.delegation, connectionCleanup);
        return connectionCleanup.getConnection(connectionMain);
    }
    public Recommendation release(VC vendorConnection, Recommendation recommendation) {
        Recommendation finalRecommendation = this.databaseAnalysis.analyzeRelease(recommendation);
        if (finalRecommendation.equals(Recommendation.FailoverDatabase_Now)) {

        }

    }

    public Connection getConnection() {
        long stamp = connectionLock.writeLock();

    }




}
