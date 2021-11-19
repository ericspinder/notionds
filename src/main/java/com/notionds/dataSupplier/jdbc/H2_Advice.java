package com.notionds.dataSupplier.jdbc;

import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.exceptions.Advice;
import com.notionds.dataSupplier.exceptions.Recommendation;
import com.notionds.dataSupplier.operational.Operational;

import java.sql.Connection;

public class H2_Advice<O extends Operational<Connection>,W extends Wrapper<Connection>> extends Advice<Connection,O,W> {

    public H2_Advice(O operational) {
        super(operational);
    }

    @Override
    protected Recommendation parseSqlState(String sqlState) {
        if (sqlState != null) {
            if ("28000".equals(sqlState)) {
                return Recommendation.Authentication_Failover;
            }
        }
        return null;
    }

    @Override
    protected Recommendation addSocketTimeout() {
        return Recommendation.Close_Closable;
    }

    @Override
    protected Recommendation examineMessage(String message) {
        return Recommendation.Close_Closable;
    }
}
