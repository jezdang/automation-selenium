package org.openweathermap.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggerHelper {
    private static final Log LOGGER = LogFactory.getLog(LoggerHelper.class);

    private enum Type {
        CHECK, ACTION, DEBUG, INFO, ERROR
    }

    public static void check(String content) {
        log(Type.CHECK, content);
    }

    public static void action(String content) {
        log(Type.ACTION, content);
    }

    public static void debug(String content) {
        log(Type.DEBUG, content);
    }

    public static void info(String content) {
        log(Type.INFO, content);
    }

    public static void error(String content) {
        log(Type.ERROR, content);
    }

    private static void log(Type type, String content) {
        LOGGER.info("[" + type.toString() + "] " + content);
    }
}
