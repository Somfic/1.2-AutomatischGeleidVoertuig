package Logic;

import Hardware.Bluetooth;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class BluetoothLogic {
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




}
