import Hardware.Bluetooth;
import Logic.BluetoothLogic;
import TI.BoeBot;
import TI.SerialConnection;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Robot robot = new Robot();

        Bluetooth serial = new Bluetooth(115200);

        BluetoothLogic bluetoothLogic = new BluetoothLogic(115200);

        while (true) {

            String input = bluetoothLogic.read();

            if(!input.isEmpty()) {
                bluetoothLogic.send(input);

                System.out.println("input: " + input);
            }
            BoeBot.wait(1);
        }
    }
}