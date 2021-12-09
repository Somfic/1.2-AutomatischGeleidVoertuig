package Behaviour.Bluetooth;
import Behaviour.Behaviour;
import Logic.BluetoothLogic;

public class BluetoothBehaviour implements Behaviour {
    private BluetoothLogic bluetoothLogic;
    private BluetoothListener bluetoothListener;

    public BluetoothBehaviour(BluetoothListener bluetoothListener, BluetoothLogic bluetoothLogic) {
        this.bluetoothListener = bluetoothListener;
        this.bluetoothLogic = bluetoothLogic;
    }

    @Override
    public void initialise() {

    }

    @Override
    public void process() {
        String input = bluetoothLogic.read();
        if(!input.isEmpty()){
            System.out.println(input);
            bluetoothListener.omBluetoothMessage(input);
        }
    }

    @Override
    public void reset() {

    }
}
