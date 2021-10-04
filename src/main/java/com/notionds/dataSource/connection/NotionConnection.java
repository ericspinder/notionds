package com.notionds.dataSource.connection;

import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.Recommendation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class NotionConnection<O extends Options> {

    private static final Logger logger = LogManager.getLogger(NotionConnection.class);
    private final O options;
    private final NotionDs vendorMain;
    private final Connection delegate;
    private boolean isOpen = true;


    public NotionConnection(O options, NotionDs vendorMain, Connection delegate) {
        this.options = options;
        this.vendorMain = vendorMain;
        this.delegate = delegate;

    }

    public Connection getDelegate() {
        return this.delegate;
    }

    public void release(Recommendation recommendation) {
        recommendation = this.vendorMain.release(this, recommendation);
        if (recommendation.shouldClose()) {
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
