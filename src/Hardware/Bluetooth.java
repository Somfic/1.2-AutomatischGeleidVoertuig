package Hardware;

import TI.SerialConnection;

public class Bluetooth {
    private SerialConnection serial;

    public Bluetooth(int baudrate) {
        serial = new SerialConnection(baudrate);
    }

    public int readByte(){
        return serial.readByte();
    }

    public boolean canRead(){
        if(this.serial.available() > 0){
            return true;
        }
        else{
            return false;

        }
    }

    public void sendByte(int message){
        this.serial.writeByte(message);
    }


}
