package Logic;

import Logger.Logger;
import Logger.LogLevel;
import Logger.LogMessage;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class BluetoothLogic implements SerialPortEventListener {

    private Logger logger = new Logger(this);

    private BluetoothListener listener;

    public BluetoothLogic(BluetoothListener listener) {
        this.listener = listener;
    }

    private SerialPort connection;

    public void send(String message) {
        try {

            logger.debug("Outgoing: " + message);

            // Write data to the Bluetooth port
            this.connection.writeString(message);

        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public void send(String message, String argument) {
        String command = message + ":" + argument;
        send(command);
    }

    public void open() {
        try {

            logger.debug("Connecting on port " + this.connection.getPortName());

            // Open the Bluetooth port
            this.connection.openPort();

            // Set the Bluetooth communication parameters
            this.connection.setParams(SerialPort.BAUDRATE_115200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            this.connection.addEventListener(this);

            logger.info("Bluetooth connected");

            this.listener.onBluetoothOpened(true);

        } catch (SerialPortException e) {
            logger.error("Could not connect to Bluetooth on port " + this.connection.getPortName());
            logger.warn(e.getMessage());
        }
    }

    public void close() {
        try {

            logger.debug("Closing on port " + this.connection.getPortName());

            this.connection.removeEventListener();

            // Close the Bluetooth port
            this.connection.closePort();

            logger.info("Bluetooth closed");

            this.listener.onBluetoothOpened(false);

        } catch (SerialPortException e) {
            logger.error("Could not close Bluetooth on port " + this.connection.getPortName());
            logger.warn(e.getMessage());
        }
    }

    public void setPort(int port) {

        logger.debug("Setting port to " + port);

        if (isConnected()) {
            // Close the Bluetooth port
            close();
        }

        this.connection = new SerialPort("COM" + port);
    }

    public boolean isConnected() {
        return this.connection != null && this.connection.isOpened();
    }

    private String messageBuffer;

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        try {
            SerialPort port = serialPortEvent.getPort();

            byte[] bytes = port.readBytes();

            if(bytes == null) {
                return;
            }

            for (byte b : bytes) {

                // Start of text, clear the buffer
                if (b == 2) {
                    messageBuffer = "";
                }

                // End of text, send buffer to callback
                else if (b == 3) {
                    BluetoothMessage message = new BluetoothMessage(messageBuffer);

                    if(message.getType().equals("log")) {

                        String[] parts =  message.getValue().split(";");

                        LogLevel level = LogLevel.valueOf(parts[0]);
                        String source = parts[1];
                        String className = parts[2];
                        String content = parts[3];

                        logger.debug("CUSTOM: " + messageBuffer);

                        logger.log(new LogMessage(source, className, level, content));
                    } else {
                        logger.info("Incoming: " + messageBuffer);
                    }

                    listener.onBluetoothMessage(message);
                }

                // Normal character
                else {
                    messageBuffer += (char) b;
                }
            }
        } catch (SerialPortException e) {
           logger.error("Could not process Bluetooth event");
           logger.warn(e.getMessage());
        }
    }
}
