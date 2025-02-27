package edu.kit.cargame.common.logging;

/**
 * The enum Log level, represents all possible log levels. Their priority is given by the integer value.
 */
public enum LogLevel {
    /**
     * Debug log level.
     */
    DEBUG(1),
    /**
     * Info log level.
     */
    INFO(2),
    /**
     * Warning log level.
     */
    WARNING(3),
    /**
     * Error log level.
     */
    ERROR(4),
    /**
     * Critical log level.
     */
    CRITICAL(5);

    private final int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    /**
     * Gets the priority of the loglevel.
     *
     * @return the priority as a number from 1 to 5
     */
    public int getPriority() {
        return priority;
    }
}
