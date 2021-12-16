package Behaviour.Bluetooth;

import Behaviour.Behaviour;
import Logger.Logger;
import Logic.BluetoothLogic;

public class BluetoothBehaviour implements Behaviour {
    private final BluetoothLogic BLUETOOTH_LOGIC;
    private final BluetoothListener BLUETOOTH_LISTENER;

    private final Logger LOGGER = new Logger(this);

    public BluetoothBehaviour(BluetoothListener bluetoothListener, BluetoothLogic bluetoothLogic) {
        this.BLUETOOTH_LISTENER = bluetoothListener;
        this.BLUETOOTH_LOGIC = bluetoothLogic;
    }

    @Override
    public void initialise() {

    }

    @Override
    public void process() {
        String input = BLUETOOTH_LOGIC.read();
        if (!input.isEmpty()) {
            LOGGER.info(input);
            BLUETOOTH_LISTENER.onBluetoothMessage(input);
        }
    }

    @Override
    public void reset() {
        BLUETOOTH_LOGIC.read();
    }
}
