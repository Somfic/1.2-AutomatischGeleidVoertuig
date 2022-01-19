package Hardware;

import TI.BoeBot;
import TI.PinMode;
import TI.SerialConnection;

public class Bluetooth {
    private final SerialConnection SERIAL;

    public Bluetooth(int baudrate) {
        SERIAL = new SerialConnection(baudrate);
    }

    //reads a recieved byte
    public int readByte() {
        return SERIAL.readByte();
    }

    //check if there is a bluetooth message to read
    public boolean canRead() {
        return this.SERIAL.available() > 0;
    }

    //sends given bytes to bluetooth reciever
    public void sendByte(int message) {
        this.SERIAL.writeByte(message);
    }

    public void reset() {
        BoeBot.setMode(15, PinMode.Output);
        BoeBot.digitalWrite(15, true);
        BoeBot.digitalWrite(15, false);
    }


}
