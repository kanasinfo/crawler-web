package com.ch.common.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerSupport {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public static void logInfo(Logger logger, Object... logs) {
        if (logger.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer();
            for (Object log : logs) {
                sb.append(log);
            }
            logger.info(sb.toString());
        }
    }

    public static void logWarn(Logger logger, Object... logs) {
        if (logger.isWarnEnabled()) {
            StringBuffer sb = new StringBuffer();
            for (Object log : logs) {
                sb.append(log);
            }
            logger.warn(sb.toString());
        }
    }

    public static void logDebug(Logger logger, Object... logs) {
        if (logger.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer();
            for (Object log : logs) {
                sb.append(log);
            }
            logger.debug(sb.toString());
        }
    }

    public static void logError(Logger logger, Object... logs) {
        if (logger.isErrorEnabled()) {
            StringBuffer sb = new StringBuffer();
            for (Object log : logs) {
                sb.append(log);
            }
            logger.error(sb.toString());
        }
    }

    public static void logTrace(Logger logger, Object... logs) {
        if (logger.isTraceEnabled()) {
            StringBuffer sb = new StringBuffer();
            for (Object log : logs) {
                sb.append(log);
            }
            logger.trace(sb.toString());
        }
    }

    protected void logInfo(Object... logs) {
        logInfo(logger, logs);
    }

    protected void logWarn(Object... logs) {
        logWarn(logger, logs);
    }

    protected void logDebug(Object... logs) {
        logDebug(logger, logs);
    }

    protected void logError(Object... logs) {
        logError(logger, logs);
    }

    protected void logTrace(Object... logs) {
        logTrace(logger, logs);
    }
}
