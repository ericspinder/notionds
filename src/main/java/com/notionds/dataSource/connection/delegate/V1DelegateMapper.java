package com.notionds.dataSource.connection.delegate;

import com.notionds.dataSource.Options;

public abstract class V1DelegateMapper<O extends Options,
                NC extends NotionConnectionDelegate,
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
