import Behaviour.*;
import Behaviour.StartStop.StartStopBehaviour;
import Behaviour.StartStop.StartStopListener;
import Hardware.Led;
import Logic.*;
import TI.BoeBot;

import java.awt.*;

public class Robot implements StartStopListener {
    private LedLogic lights = new LedLogic();
    private BuzzerLogic buzzer = new BuzzerLogic(2);
    private MotorLogic motors = new MotorLogic(12, 13);
    private InfraredLogic infrared = new InfraredLogic(3);

    private MovementBehaviour movementBehaviour = new MovementBehaviour(motors, infrared);
    private BlinkerBehaviour blinkerBehaviour = new BlinkerBehaviour(lights, motors);
    private RemoteBehaviour remoteBehaviour = new RemoteBehaviour(infrared);
    private BuzzerBehaviour buzzerBehaviour = new BuzzerBehaviour(buzzer, infrared, motors);

    private Logic[] logics = {lights, buzzer, motors, infrared};
    private Behaviour[] behaviours = {movementBehaviour, blinkerBehaviour, remoteBehaviour, buzzerBehaviour};

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
        System.out.println(shouldStop);

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
            behaviour.process();
        }

        for (Logic logic : this.logics) {
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
