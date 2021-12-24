package Logger;


public class Logger {

    private static LoggerListener listener;
    public static void setListener(LoggerListener listener) {
        Logger.listener = listener;
    }

    private static String source;
    public static void setSource(String source) {
        Logger.source = source;
    }

    private final String NAME;

    /**
     * Creates a new logger for the given class instance
     *
     * @param classInstance The instance of the class to log for
     */
    public Logger(Object classInstance) {
        this.NAME = classInstance.getClass().getSimpleName();
    }

    /**
     * Logs a message at the DEBUG level
     *
     * @param message The message to log
     */
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    /**
     * Logs a message at the INFO level
     *
     * @param message The message to log
     */
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    /**
     * Logs a message at the WARNING level
     *
     * @param message The message to log
     */
    public void warn(String message) {
        log(LogLevel.WARNING, message);
    }

    /**
     * Logs a message at the ERROR level
     *
     * @param message The message to log
     */
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    /**
     * Logs a message with the given level
     *
     * @param logLevel The level of the message
     * @param message  The message to log
     */
    @SuppressWarnings("Duplicates") // BoeBot uploader doesn't allow dependencies in other modules, so code duplication is necessarily
    public void log(LogLevel logLevel, String message) {
        LogMessage logMessage = new LogMessage(Logger.source, this.NAME, logLevel, message);

        Logger.listener.onLogMessage(logMessage);

        // Print the log message
        String content = "[" + String.format("%1$5s", logLevel.toString()) + "] ["+String.format("%1$5s", Logger.source)+"] [" + this.NAME + "] " + message;
        System.out.println(content);
    }
}

