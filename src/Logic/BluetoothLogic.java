package Logic;

import Hardware.Bluetooth;


public class    BluetoothLogic implements Logic {
    private Bluetooth bluetooth;

    public BluetoothLogic(int baudrate) {
        this.bluetooth = new Bluetooth(baudrate);
    }

    public String read() {
        if(bluetooth.canRead()){
            int input = bluetooth.readByte();
            return Character.toString((char)input);
        }
        else{
            return "";
        }
    }

    public void send(String message){
        for(int i =0; i < message.length(); i++){
            send(message.charAt(i));
        }
    }

    public void send(char letter){
        this.bluetooth.sendByte(letter);
    }


    @Override
    public void process() {

    }

    @Override
    public void reset() {

    }
}
