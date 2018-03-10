package com.notionds.dataSource.connection.v1;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.connection.DelegateMapper;
import com.notionds.dataSource.connection.ExceptionHandler;
import com.notionds.dataSource.connection.NotionConnection;

import java.io.InputStream;
import java.io.Reader;
import java.sql.*;

public abstract class V1DelegateMapper<O extends Options,
                NC extends NotionConnection,
                EH extends ExceptionHandler,
                SD extends StatementDelegate,
                PSD extends PreparedStatementDelegate,
                CSD extends CallableStatementDelegate,
                RSD extends ResultSetDelegate,
                DMD extends DatabaseMetaDataDelegate,
                CBD extends ClobDelegate,
                NCD extends NClobDelegate,
                BBD extends BlobDelegate> extends DelegateMapper<O, NC, EH, SD, PSD, CSD, RSD, DMD, CBD, NCD, BBD> {

    public V1DelegateMapper(O options, EH exceptionHandler) {
        super(options, exceptionHandler);
    }
}
