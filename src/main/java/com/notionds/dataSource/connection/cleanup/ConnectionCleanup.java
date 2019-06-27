package com.notionds.dataSource.connection.cleanup;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.Recommendation;
import com.notionds.dataSource.connection.ConnectionContainer;
import com.notionds.dataSource.connection.VendorConnection;
import com.notionds.dataSource.connection.delegation.ConnectionMember_I;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;

public abstract class ConnectionCleanup<O extends Options, VC extends VendorConnection>  {

    protected static final Logger logger = LoggerFactory.getLogger(ConnectionCleanup.class);

    protected final O options;
    protected final VC vendorConnection;
    protected boolean keepOpen = true;

    public ConnectionCleanup(O options, VC vendorConnection) {
        this.options = options;
        this.vendorConnection = vendorConnection;
    }

    public abstract Connection getConnection(ConnectionContainer connectionContainer);

    public void cleanup(ConnectionMember_I connectionMember, Object delegate) {
        if (!(connectionMember instanceof Connection) && !this.keepOpen) {
            this.doDelegateClose(delegate);
        }
        this.clear(connectionMember);
    }
    protected abstract void clear(ConnectionMember_I connectionMember);


    public abstract ConnectionMember_I add(ConnectionMember_I connectionMember, Object delegate, ConnectionMember_I parent);

    public void reviewException(ConnectionMember_I connectionMember, NotionExceptionWrapper exceptionWrapper) {
        this.vendorConnection.release(exceptionWrapper.getRecommendation());
        if (!exceptionWrapper.getRecommendation().equals(Recommendation.NoAction)) {
            this.keepOpen = false;
        }

    }
    private void doDelegateClose(Object delegate) {
        try {
            if (delegate instanceof AutoCloseable) {
                ((AutoCloseable)delegate).close();
            }
            else if (delegate instanceof Clob) {
                ((Clob)delegate).free();
            }
            else if (delegate instanceof Blob) {
                ((Blob)delegate).free();
            }
            else if (delegate instanceof Array) {
                ((Array)delegate).free();
            }
        }
        catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exception trying to clean up Reference was ignored: ");
            printCause(e, stringBuilder);
            logger.error(stringBuilder.toString());
        }
    }

    protected void printCause(Throwable t, StringBuilder stringBuilder) {
        stringBuilder.append(t.getMessage());
        if (t.getCause() != null) {
            stringBuilder.append("\n caused by: ");
            printCause(t.getCause(), stringBuilder);
        }
    }

}
