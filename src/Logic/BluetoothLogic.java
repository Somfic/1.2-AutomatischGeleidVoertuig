package Logic;

import Hardware.Bluetooth;

public class BluetoothLogic implements Logic {
    private final Bluetooth BLUETOOTH;

    public BluetoothLogic(int baudrate) {
        this.BLUETOOTH = new Bluetooth(baudrate);
    }

    //reads an input from BLUETOOTH as a string
    public String read() {
        if (BLUETOOTH.canRead()) {
            String output = "";

            while(BLUETOOTH.canRead()) {
                int input = BLUETOOTH.readByte();
                output += (char) input;
            }

            return output;
        } else {
            return "";
        }
    }

    //send a string to a receiver
    public void send(String message) {

        // Start of text
        send(2);

        for (int i = 0; i < message.length(); i++) {
            send((int) message.charAt(i));
        }

        // End of text
        send(3);
    }

    public void send(String message, String argument) {
        String command = message + ":" + argument;
        send(command);
    }

    //send a single character to a receiver
    private void send(int value) {
        this.BLUETOOTH.sendByte(value);
    }


    @Override
    public void process() {

    }

    @Override
    public void reset() {
        BLUETOOTH.reset();
        read();
    }
}
