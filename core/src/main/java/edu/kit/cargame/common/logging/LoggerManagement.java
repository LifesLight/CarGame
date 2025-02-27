package edu.kit.cargame.common.logging;

import java.util.HashMap;

/**
 * The type Logger management.
 */
public final class LoggerManagement {
    private static final HashMap<String, Logger> LOGGERS = new HashMap<>();
    private static LogLevel defaultMinimalLevel = LogLevel.INFO;

    private LoggerManagement() {
        //Do not instantiate
    }

    /**
     * Get a logger with a name and the default minimal level set in logger management.
     * If such a logger with that name already exists, it is returned, otherwise a new one is created
     *
     * @param name          the name for the logger
     * @param minimalLevel the minimal level
     * @return the logger
     */
    private static Logger getLogger(String name, LogLevel minimalLevel) {
        if (LOGGERS.containsKey(name)) {
            LOGGERS.get(name).setMinimalLevel(minimalLevel);
            return LOGGERS.get(name);
        }
        Logger logger = new Logger(name, minimalLevel);
        LOGGERS.put(name, logger);
        return logger;
    }

    /**
     * Get or create a logger using the callers file name as the logging name and the default minimal log level.
     *
     * @return the logger
     */
    public static Logger getLogger() {
        // Get file name of calling file
        // Write it this way, so IntelliJ doesnt say it does the same thing as getLogger, as this would give a wrong name
        // Because the stacktrace grows with each method call
        String name = Thread.currentThread().getStackTrace()[2].getFileName();
        return getLogger(name, defaultMinimalLevel);
    }

    /**
     * Get or create a logger using the callers file name as the logging name.
     *
     * @param minimalLevel the minimal level
     * @return the logger
     */
    public static Logger getLogger(LogLevel minimalLevel) {
        // Get file name of calling file
        String name = Thread.currentThread().getStackTrace()[2].getFileName();
        return getLogger(name, minimalLevel);
    }

    /**
     * Sets default minimal level for all loggers which are created without an explicit loglevel.
     *
     * @param level the priority level
     */
    public static void setDefaultMinimalLevel(LogLevel level) {
        defaultMinimalLevel = level;
    }

    /**
     * Gets current default minimal level.
     *
     * @return the default minimal level
     */
    public static LogLevel getDefaultMinimalLevel() {
        return defaultMinimalLevel;
    }
}
