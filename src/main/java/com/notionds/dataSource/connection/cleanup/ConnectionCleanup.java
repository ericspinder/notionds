package com.notionds.dataSource.connection.cleanup;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.Timer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;

public abstract class ConnectionCleanup<O extends Options>  {

    protected static final Logger logger = LogManager.getLogger(ConnectionCleanup.class);

    protected final O options;

    protected final Timer timer = new Timer();

    public ConnectionCleanup(O options) {
        this.options = options;
    }

    public Timer getTimer() {
        return this.timer;
    }

    public abstract Connection getConnection();

//    public Recommendation cleanup(ConnectionArtifact_I connectionArtifact, Object delegate) {
//        if (connectionArtifact instanceof Connection) {
//            this.connectionWeakReference.release(Recommendation.ReturnToPool);
//        }
//        else {
//            DoDelegateClose(delegate);
//        }
//        this.empty(connectionArtifact);
//    }
    public abstract boolean empty(ConnectionArtifact_I connectionMember);
    public abstract boolean emptyAll();

    public abstract ConnectionArtifact_I add(ConnectionArtifact_I connectionMember, Object delegate, ConnectionArtifact_I parent);

    public static void DoDelegateClose(Object delegate) {
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
            PrintCause(e, stringBuilder);
            logger.error(stringBuilder.toString());
        }
    }

    protected static void PrintCause(Throwable t, StringBuilder stringBuilder) {
        stringBuilder.append(t.getMessage());
        if (t.getCause() != null) {
            stringBuilder.append("\n caused by: ");
            PrintCause(t.getCause(), stringBuilder);
        }
    }

}
