package com.notionds.dataSource;

public abstract class DatabaseAnalysis<O extends Options> {

    protected final O options;

    public DatabaseAnalysis(O options) {
        this.options = options;
    }

    public abstract Recommendation analyzeRelease(Recommendation recommendation);

}
