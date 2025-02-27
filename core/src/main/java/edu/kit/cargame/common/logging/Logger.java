package edu.kit.cargame.common.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The type Logger.
 */
public class Logger {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // Log format with line number padded to 3 characters and loglevel to 6
    // Time stamp, class name, line number, loglevel, message
    private String loggingFormat = "<%s> |%s:%-3s| [%-8s]: %s";
    private LogLevel minimalLevel;
    private final String name;

    /**
     * Instantiates a new Logger.
     *
     * @param name           the name of the logger
     * @param minimalLevel  the minimal level, all messages with higher or equal priority level will be logged
     * @param loggingFormat the logging format
     */
    public Logger(String name, LogLevel minimalLevel, String loggingFormat) {
        this.name = name;
        this.minimalLevel = minimalLevel;
        this.loggingFormat = loggingFormat;
    }

    /**
     * Instantiates a new Logger.
     *
     * @param name           the name of the logger
     */
    public Logger(String name) {
        this.name = name;
    }

    /**
     * Instantiates a new Logger.
     *
     * @param name           the name of the logger
     * @param minimalLevel  the minimal level, all messages with higher or equal priority level will be logged
     */
    public Logger(String name, LogLevel minimalLevel) {
        this.name = name;
        this.minimalLevel = minimalLevel;
    }

    /**
     * Log a message, if its level is higher than the current minimum level.
     *
     * @param message the message to log
     * @param level   the priority level of the message
     */
    private void log(String message, LogLevel level) {
        if (level.getPriority() < minimalLevel.getPriority()) {
            return;
        }
        int callersLineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        String timestamp = dateFormat.format(new Date());
        String logMessage = loggingFormat.formatted(timestamp, name, callersLineNumber, level, message);
        System.out.println(logMessage);
    }

    /**
     * Log a message with loglevel Debug.
     *
     * @param message the message
     */
    public void debug(String message) {
        log(message, LogLevel.DEBUG);
    }

    /**
     * Log a message with loglevel Info.
     *
     * @param message the message
     */
    public void info(String message) {
        log(message, LogLevel.INFO);
    }

    /**
     * Log a message with loglevel Warning.
     *
     * @param message the message
     */
    public void warning(String message) {
        log(message, LogLevel.WARNING);
    }

    /**
     * Log a message with loglevel Error.
     *
     * @param message the message
     */
    public void error(String message) {
        log(message, LogLevel.ERROR);
    }

    /**
     * Log a message with loglevel Critical.
     *
     * @param message the message
     */
    public void critical(String message) {
        log(message, LogLevel.CRITICAL);
    }

    /**
     * Set the minimal level of the logger.
     * @param minimalLevel new minimal level
     */
    public void setMinimalLevel(LogLevel minimalLevel) {
        this.minimalLevel = minimalLevel;
    }
}
