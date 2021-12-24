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
    @SuppressWarnings("Duplicates") // BoeBot uploader doesn't allow dependencies in other modules, so code
                                    // duplication is necessarily
    public void log(LogLevel logLevel, String message) {
        LogMessage logMessage = new LogMessage(Logger.source, this.NAME, logLevel, message);
        log(logMessage);
    }

    public void log(LogMessage message) {
        Logger.listener.onLogMessage(message);
        print(message);
    }

    private void print(LogMessage message) {
        String content = "";

        String gray = "\u001B[90m";
        String red = "\u001B[31m";
        String green = "\u001B[32m";
        String yellow = "\u001B[33m";
        String blue = "\u001B[34m";
        String magenta = "\u001B[35m";
        String cyan = "\u001B[36m";
        String white = "\u001B[37m";

        // Print the log level
        switch (message.getLevel()) {
            case DEBUG:
                content += gray + "[" + green + "DEBUG" + gray + "]";
                break;
            case INFO:
                content += gray + "[" + blue + " INFO" + gray + "]";
                break;
            case WARNING:
                content += gray + "[" + yellow + "WARN" + gray + "]";
                break;
            case ERROR:
                content += gray + "[" + red + "ERROR" + gray + "]";
                break;
        }

        // Print the source of the message
        content += " " + gray + "[" + cyan + message.getSource() + gray + ":";

        // Print the name of the class
        content += magenta + message.getClassName() + gray + "]";

        // Print the message
        content += " " + white + message.getMessage();

        System.out.println(content);
    }
}
