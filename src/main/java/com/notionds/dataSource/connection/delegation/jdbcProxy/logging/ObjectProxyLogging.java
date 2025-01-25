package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.time.Instant;

import static com.notionds.dataSource.Options.Strings.Logging_Method_REGEX;

public class ObjectProxyLogging {

    private final Logger logger = LogManager.getLogger(ObjectProxyLogging.class);
    protected final LoggingService loggingService;

    public ObjectProxyLogging(LoggingService loggingService) {
        this.loggingService = loggingService;
    }
    public InvokeAccounting startInvoke(Method m, Object[] args) {
        if (m.getName().matches((String) loggingService.options.get(Logging_Method_REGEX.getKey()))) {
            logger.trace("Logging invoke on method = " + m.getName());
            return loggingService.newInvokeAccounting();
        }
        return null;
    }

    public void exception(NotionExceptionWrapper notionExceptionWrapper, String description, Method method, InvokeAccounting invokeAccounting) {
        loggingService.populateException(notionExceptionWrapper, description, method, invokeAccounting);
    }

    public void endInvoke(Method m, String description, InvokeAccounting invokeAccounting) {
        logger.trace("end invoke - " + m.getName() + " description: " + description);
        invokeAccounting.setFinishTime(Instant.now());
        loggingService.populateExecution(m,  description, invokeAccounting);
    }

}
