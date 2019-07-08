package com.notionds.dataSource.connection;

import com.notionds.dataSource.VendorMain;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class VendorConnection<O extends Options> {

    private static final Logger logger = LoggerFactory.getLogger(VendorConnection.class);
    private final O options;
    private final VendorMain vendorMain;
    private final Connection delegate;
    private boolean isOpen = true;


    public VendorConnection(O options, VendorMain vendorMain, Connection delegate) {
        this.options = options;
        this.vendorMain = vendorMain;
        this.delegate = delegate;

    }
    protected VendorMain getVendorMain() {
        return this.vendorMain;
    }

    public Connection getDelegate() {
        return this.delegate;
    }

    public void release(Recommendation recommendation) {
        recommendation = this.vendorMain.release(this, recommendation);
        if (recommendation.isCloseVendorConnection()) {
            try {
                if (this.delegate != null && !this.delegate.isClosed())
                this.delegate.close();
            }
            catch (SQLException sqle) {
                logger.info("Error on connection close (no action needed?)" + sqle.getMessage());
            }
        }


    }

    public boolean isOpen() {
        return this.isOpen;
    }


}
