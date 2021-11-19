package com.notionds.dataSupplier.delegation.refelction.logging;

import com.notionds.dataSupplier.delegation.Wrapper;
import com.notionds.dataSupplier.operational.Operational;
import com.notionds.dataSupplier.delegation.refelction.InterfaceReflection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.util.function.Supplier;

public class InterfaceReflectionWithLogging<N, O extends Operational, W extends Wrapper<N> & InvocationHandler> extends InterfaceReflection<N,O,W> {

    private static final Logger logger = LogManager.getLogger(InterfaceReflectionWithLogging.class);

    public static class Default<N, W extends Wrapper<N> & InvocationHandler> extends InterfaceReflectionWithLogging<N,Operational.Default,W> {

        public Default(Supplier<W> proxySupplier) {
            super(Operational.DEFAULT_OPTIONS_INSTANCE, proxySupplier, LoggingService.DEFAULT_INSTANCE);
        }
    }

    private final LoggingService<?,?,?,?,?> loggingService;

    public InterfaceReflectionWithLogging(O options, Supplier<W> proxySupplier, LoggingService<?,?,?,?,?> loggingService) {
        super(options, proxySupplier);
        this.loggingService = loggingService;
    }
// , DL extends ObjectProxyLogging<?, ?, ?>, SL extends StatementLogging<?, ?,?>, PL extends PreparedStatementLogging<?, ? ,?>
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public <D> Proxy<D> createProxyMember(Container<?,?,?,?> container, D delegate, Object[] args) {
//        if (delegate instanceof PreparedStatement) {
//            return new ProxyLogging<D, PL>(container, delegate, (PL) this.loggingService.newPreparedStatementLogging((String) args[0]));
//        }
//        else if (delegate instanceof Statement){
//            return new ProxyLogging<D, SL>(container, delegate, (SL) this.loggingService.newStatementLogging());
//        }
//        else if (this.operational.getBoolean(BooleanOption.LogNonExecuteProxyMembers.getKey())) {
//            return new ProxyLogging<D, DL>(container, delegate, (DL) this.loggingService.newObjectProxyLogging());
//        }
//        else {
//            return super.createProxyMember(container, delegate, args);
//        }
//    }

}
