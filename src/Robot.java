import Behaviour.*;
import Behaviour.Bluetooth.BluetoothBehaviour;
import Behaviour.Bluetooth.BluetoothListener;
import Behaviour.Buzzer.*;
import Behaviour.Distance.DistanceBehaviour;
import Behaviour.Gripper.GripperBehaviour;
import Behaviour.Lights.*;
import Behaviour.Movement.*;
import Behaviour.Remote.*;
import Behaviour.StartStop.*;
import Hardware.Led;
import Logic.*;
import TI.BoeBot;

import java.awt.*;

public class Robot implements StartStopListener {
    private LedLogic lights = new LedLogic();
    private BuzzerLogic buzzer = new BuzzerLogic(2);
    private MotorLogic motors = new MotorLogic(12, 13);
    private InfraredLogic infrared = new InfraredLogic(3);
    private DistanceLogic distance = new DistanceLogic(10, 11);
    private WhiskerLogic whiskers = new WhiskerLogic(11, 15);
    private BluetoothLogic bluetooth = new BluetoothLogic(115200);
    private GripperLogic grippers = new GripperLogic(14);

    private MovementBehaviour movementBehaviour = new MovementBehaviour(motors, distance);
    private LightsBehaviour blinkerBehaviour = new LightsBehaviour(lights, motors);
    private RemoteBehaviour remoteBehaviour = new RemoteBehaviour(movementBehaviour, infrared);
    private BuzzerBehaviour buzzerBehaviour = new BuzzerBehaviour(buzzer, infrared, motors);
    private DistanceBehaviour distanceBehaviour = new DistanceBehaviour(distance);
    private BluetoothBehaviour bluetoothBehaviour = new BluetoothBehaviour(movementBehaviour, this.bluetooth);
    private GripperBehaviour gripperBehaviour = new GripperBehaviour(grippers, infrared);

    private Logic[] logics = {lights, buzzer, motors, infrared, distance, bluetooth, grippers};
    private Behaviour[] behaviours = {movementBehaviour, blinkerBehaviour, remoteBehaviour, buzzerBehaviour, distanceBehaviour, bluetoothBehaviour, gripperBehaviour};

    private StartStopBehaviour startStopBehaviour = new StartStopBehaviour(this, infrared);

    private Led indicatorLed = new Led(1);

    public void run() {

        initialiseAll();
        resetAll();

        // Main robot loop
        while (true) {
            BoeBot.wait(1);

            // Process the start/stop behaviour separately from the rest
            // of the behaviours so that the robot can be stopped at any time
            startStopBehaviour.process();

            if(!startStopBehaviour.shouldStop()) {
               processAll();
            }
        }
    }

    @Override
    public void onStartStop(boolean shouldStop) {
        if(shouldStop) {

            resetAll();

            // TODO: move this to its own logic/behaviour?
            this.indicatorLed.set(Color.red);
        }
    }

    private void initialiseAll() {

        this.startStopBehaviour.initialise();

        for (Behaviour behaviour : this.behaviours) {
            behaviour.initialise();
        }
    }

    private void processAll() {

        for (Behaviour behaviour : this.behaviours) {
//            System.out.println("behavior: " + behaviour.getClass().getSimpleName());
            behaviour.process();
        }

        for (Logic logic : this.logics) {
//            System.out.println("logic: " + logic.getClass().getSimpleName());
            logic.process();
        }
    }

    private void resetAll() {
        for (Behaviour behaviour : this.behaviours) {
            behaviour.reset();
        }

        for (Logic logic : this.logics) {
            logic.reset();
        }
    }

}
