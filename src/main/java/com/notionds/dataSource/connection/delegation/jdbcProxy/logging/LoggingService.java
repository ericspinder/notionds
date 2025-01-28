package com.notionds.dataSource.connection.delegation.jdbcProxy.logging;

import com.notionds.dataSource.Options;
import com.notionds.dataSource.exceptions.NotionExceptionWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Objects;

import static com.notionds.dataSource.Options.Strings.Logging_Method_REGEX;

public class LoggingService {

    private static final Logger logger = LogManager.getLogger(LoggingService.class);
    protected final Options options;
    protected final String loggerName;

    @SuppressWarnings("unchecked")
    public LoggingService(String loggerName, Options options) {
        this.loggerName = loggerName;
        this.options = options;
    }


    public InvokeAccounting startInvoke(Method m, Object[] args, String sql) {
        if (m.getName().matches((String) this.options.get(Logging_Method_REGEX.getKey()))) {
            if (args != null && args[0] instanceof String) {
                return new InvokeAccounting(m.getName(), (String) args[0]);
            }
            else return new InvokeAccounting(m.getName(), Objects.requireNonNullElse(sql, "unknown sql"));
        }
        return null;
    }
    private String enableMask(String unmasked) {
        return unmasked.replace((String) options.get(Options.Strings.Logging_Replace_Regex.getKey()),(String) options.get(Options.Strings.Logging_Mask.getKey()));
    }

    public void populateExecution(InvokeAccounting invokeAccounting) {
        invokeAccounting.setFinishTime(Instant.now());
        if ((Boolean)options.get(Options.Booleans.Enable_Masking.getKey())) {
            logger.info("[" +loggerName +  "] " + enableMask(invokeAccounting.toString()));
        }
        else {
            logger.info("[" +loggerName +  "] " + invokeAccounting);
        }
    }
    public void populateThrownException(NotionExceptionWrapper notionExceptionWrapper, InvokeAccounting invokeAccounting) {
        invokeAccounting.setFinishTime(Instant.now());
        String invokeString = (Boolean) options.get(Options.Booleans.Enable_Masking.getKey()) ? enableMask(invokeAccounting.toString()):invokeAccounting.toString();
        if (notionExceptionWrapper.getCause() != null) {
            logger.error("[" +loggerName +  "] Exception cause = " + notionExceptionWrapper.getCause().getMessage() + ", " + invokeString + ", Recommendation = " + notionExceptionWrapper.getRecommendation());
        }
        logger.info("[" +loggerName +  "] Notion Exception = " + notionExceptionWrapper.getMessage() + ", " + invokeString + ", Recommendation = " + notionExceptionWrapper.getRecommendation());
    }
}
