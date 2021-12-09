package Logic;

import Hardware.Bluetooth;


public class BluetoothLogic implements Logic {
    private final Bluetooth bluetooth;

    public BluetoothLogic(int baudrate) {
        this.bluetooth = new Bluetooth(baudrate);
    }

    //reads an input from bluetooth as a string
    public String read() {
        if (bluetooth.canRead()) {
            int input = bluetooth.readByte();
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
        this.bluetooth.sendByte(letter);
    }


    @Override
    public void process() {

    }

    @Override
    public void reset() {
        bluetooth.reset();
    }
}
