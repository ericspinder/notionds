package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamConnectionArtifact extends OutputStream implements ConnectionArtifact_I<OutputStream> {

    protected final InnerState<OutputStream> innerState;

    public OutputStreamConnectionArtifact(OutputStream delegate,ConnectionContainer connectionContainer) {
        this.innerState = new InnerState<>(delegate,connectionContainer);
    }

    @Override
    public InnerState<OutputStream> getInnerState() {
        return innerState;
    }

    @Override
    public void write(int b) throws IOException {
        try {
            getDelegate().write(b);
        }
        catch(IOException ioe) {
            throw (IOException) getConnectionContainer().getConnectionPool().throwBackProcessedException(ioe,this);
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            getDelegate().flush();
        }
        catch(IOException ioe) {
            throw (IOException) getConnectionContainer().getConnectionPool().throwBackProcessedException(ioe,this);
        }
    }

}
