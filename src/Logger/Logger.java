package Logger;

public class Logger {

    private final String name;

    /**
     * Creates a new logger for the given class instance
     * @param classInstance The instance of the class to log for
     */
    public Logger(Object classInstance) {
        this.name = classInstance.getClass().getSimpleName();
    }

    /**
     * Logs a message at the DEBUG level
     * @param message The message to log
     */
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    /**
     * Logs a message at the INFO level
     * @param message The message to log
     */
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    /**
     * Logs a message at the WARNING level
     * @param message The message to log
     */
    public void warn(String message) {
        log(LogLevel.WARNING, message);
    }

    /**
     * Logs a message at the ERROR level
     * @param message The message to log
     */
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    /**
     * Logs a message with the given level
     * @param logLevel The level of the message
     * @param message The message to log
     */
    public void log(LogLevel logLevel, String message) {
        // Log the message between square brackets
        System.out.println("[" + logLevel.toString() + "] [" + this.name + "] " + message);
    }
}

