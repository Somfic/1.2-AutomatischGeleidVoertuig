package Hardware;

import TI.BoeBot;
import TI.PinMode;
import TI.SerialConnection;

public class Bluetooth {
    private SerialConnection serial;

    public Bluetooth(int baudrate) {
        serial = new SerialConnection(baudrate);
    }

    //reads a recieved byte
    public int readByte(){
        return serial.readByte();
    }

    //check if there is a bluetooth message to read
    public boolean canRead(){
        if(this.serial.available() > 0){
            return true;
        }
        else{
            return false;

        }
    }

    //sends given bytes to bluetooth reciever
    public void sendByte(int message){
        this.serial.writeByte(message);
    }

    public void reset(){
        BoeBot.setMode(15, PinMode.Output);
        BoeBot.digitalWrite(15, true);
        BoeBot.digitalWrite(15, false);
    }


}
