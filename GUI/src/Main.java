import jssc.SerialPort;
import jssc.SerialPortException;

public class Main {
    private static final String PORT = "COM4";

    public static void main(String[] args) {
        SerialPort serialPort = new SerialPort(PORT);
        try {
            serialPort.openPort(); // Open the serial connection
            serialPort.setParams(SerialPort.BAUDRATE_115200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.writeString("Hello student!");
            byte[] buffer = serialPort.readBytes(10); // Fixed buffer length
            for (int i = 0; i < 10; i++)
                System.out.print(buffer[i] + "-");
            serialPort.closePort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}
