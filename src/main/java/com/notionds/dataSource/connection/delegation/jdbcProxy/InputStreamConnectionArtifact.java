package com.notionds.dataSource.connection.delegation.jdbcProxy;

import com.notionds.dataSource.ConnectionContainer;
import com.notionds.dataSource.connection.delegation.ConnectionArtifact_I;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamConnectionArtifact extends InputStream implements ConnectionArtifact_I<InputStream> {

    protected final InnerState<InputStream> innerState;
    public InputStreamConnectionArtifact(InputStream delegate, ConnectionContainer connectionContainer) {
        this.innerState = new InnerState<>(delegate,connectionContainer);
    }

    @Override
    public InnerState<InputStream> getInnerState() {
        return innerState;
    }

    @Override
    public int read() throws IOException {
        try {
            return getDelegate().read();
        }
        catch (IOException ioe) {
            throw (IOException) getConnectionContainer().getConnectionPool().throwBackProcessedException(ioe, this);
        }
    }

    @Override
    public void close() throws IOException {
        this.getDelegate().close();
    }
}
