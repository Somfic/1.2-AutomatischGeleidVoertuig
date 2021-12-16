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
            int input = BLUETOOTH.readByte();
            return Character.toString((char) input);
        } else {
            return "";
        }
    }

    //send a string to a receiver
    public void send(String message) {
        for (int i = 0; i < message.length(); i++) {
            send(message.charAt(i));
        }
    }

    //send a single character to a receiver
    public void send(char letter) {
        this.BLUETOOTH.sendByte(letter);
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
