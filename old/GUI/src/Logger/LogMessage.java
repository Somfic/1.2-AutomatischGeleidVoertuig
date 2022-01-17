package Logger;

public class LogMessage {

    private String source;

    private String className;

    private LogLevel level;

    private String message;

    public LogMessage(String source, String className, LogLevel level, String message) {
        this.source = source;
        this.className = className;
        this.level = level;
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public String getClassName() {
        return className;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
       return "[" + String.format("%1$5s", this.level.toString()) + "] [" + this.source + "] [" + this.className + "] " + message;
    }
}
