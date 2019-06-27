package com.notionds.dataSource.connection;

import com.notionds.dataSource.DatabaseMain;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class VendorConnection<O extends Options> {

    private static final Logger logger = LoggerFactory.getLogger(VendorConnection.class);
    private final O options;
    private final DatabaseMain databaseMain;
    private final Connection delegate;
    private boolean isOpen = true;


    @SuppressWarnings("unchecked")
    public VendorConnection(O options, DatabaseMain databaseMain, Connection delegate) {
        this.options = options;
        this.databaseMain = databaseMain;
        this.delegate = delegate;

    }
    protected DatabaseMain getDatabaseMain() {
        return this.databaseMain;
    }

    public Connection getDelegate() {
        return this.delegate;
    }

    public void release(Recommendation recommendation) {
        if (recommendation.isCloseVendorConnection()) {
            try {
                if (this.delegate != null && !this.delegate.isClosed())
                this.delegate.close();
            }
            catch (SQLException sqle) {
                logger.info("Error on connection close (no action needed?)" + sqle.getMessage());
            }
        }
        this.databaseMain.release(this, recommendation);

    }

    public boolean isOpen() {
        return this.isOpen;
    }


}
