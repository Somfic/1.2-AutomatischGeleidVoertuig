package Logic;

import jssc.SerialPort;
import jssc.SerialPortException;

public class BluetoothLogic {

    public BluetoothLogic() {

    }

    private SerialPort connection;

    public void send(String message) {
//        try {
//
//            // Write data to the Bluetooth port
//            this.connection.writeString(message);
//
//        } catch (SerialPortException e) {
//            e.printStackTrace();
//        }
    }

    public void send(String message, String argument) {
        String command = message + ": " + argument;
        send(command);
    }

    public String receive() {
        try {

            // Read data from the Bluetooth port
            return this.connection.readString();

        } catch (SerialPortException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void open() {
        try {

            // Open the Bluetooth port
            this.connection.openPort();

            // Set the Bluetooth communication parameters
            this.connection.setParams(SerialPort.BAUDRATE_115200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {

            // Close the Bluetooth port
            this.connection.closePort();

        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public void setPort(int port) {
        try {

            // Close the Bluetooth port
            this.connection.closePort();

        } catch (SerialPortException e) {
            e.printStackTrace();
        }

        this.connection = new SerialPort("COM" + port);
    }

    public boolean isConnected() {
        return this.connection != null && this.connection.isOpened();
    }
}
