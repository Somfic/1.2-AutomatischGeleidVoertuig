package Behaviour.Bluetooth;

import Behaviour.Behaviour;
import Logger.Logger;
import Logger.LoggerListener;
import Logger.LogMessage;
import Logic.BluetoothLogic;
import TI.Timer;

public class BluetoothBehaviour implements Behaviour, LoggerListener {
    private final BluetoothLogic BLUETOOTH_LOGIC;
    private final BluetoothListener BLUETOOTH_LISTENER;

    private final Logger LOGGER = new Logger(this);

    private final Timer TIMER = new Timer(250);

    public BluetoothBehaviour(BluetoothListener bluetoothListener, BluetoothLogic bluetoothLogic) {
        this.BLUETOOTH_LISTENER = bluetoothListener;
        this.BLUETOOTH_LOGIC = bluetoothLogic;
    }

    @Override
    public void initialise() {

    }

    @Override
    public void process() {
        if(TIMER.timeout()) {
            TIMER.mark();

            String input = BLUETOOTH_LOGIC.read();
            if (!input.isEmpty()) {
                LOGGER.info("Received: " + input);
                BLUETOOTH_LISTENER.onBluetoothMessage(input);
            }
        }
    }

    @Override
    public void reset() {
        BLUETOOTH_LOGIC.read();
    }

    @Override
    public void onLogMessage(LogMessage logMessage) {
        this.BLUETOOTH_LOGIC.send("log", logMessage.toString());
    }
}
