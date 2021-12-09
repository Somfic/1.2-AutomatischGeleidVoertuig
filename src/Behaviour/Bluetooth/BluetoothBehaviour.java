package Behaviour.Bluetooth;
import Behaviour.Behaviour;
import Logger.Logger;
import Logic.BluetoothLogic;

public class BluetoothBehaviour implements Behaviour {
    private BluetoothLogic bluetoothLogic;
    private BluetoothListener bluetoothListener;

    private Logger logger = new Logger(this);

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
            logger.info(input);
            bluetoothListener.onBluetoothMessage(input);
        }
    }

    @Override
    public void reset() {
        bluetoothLogic.read();
    }
}
