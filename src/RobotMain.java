import Hardware.*;
import Logic.*;
import TI.*;

import Behaviour.*;

import java.awt.*;

public class RobotMain {
    static private Switch startButton = new Switch(1);
    static private Switch stopButton = new Switch(0);

    static private LedLogic lights = new LedLogic();
    static private BuzzerLogic buzzer = new BuzzerLogic();
    static private MotorLogic motors = new MotorLogic(12, 13);

    static private MovementBehaviour movementBehaviour = new MovementBehaviour(motors);
    static private BlinkerBehaviour blinkerBehaviour = new BlinkerBehaviour(lights, motors);

    static private Logic[] logics = {lights, buzzer, motors};
    static private Behaviour[] behaviours = {movementBehaviour, blinkerBehaviour};

    static private EmergencyBehaviour emergencySystem = new EmergencyBehaviour();

    static private Led indicatorLed = new Led(1);

    static private boolean hasStopped;

    public static void main(String[] args) {

        for (Behaviour behaviour : behaviours) {
            behaviour.initialise();
        }

        emergencySystem.initialise();

        while (true) {
            BoeBot.wait(1);

            emergencySystem.process();

            if(emergencySystem.shouldStop() && !hasStopped) {
                hasStopped = true;

                for (Behaviour behaviour : behaviours) {
                    behaviour.reset();
                }

                for (Logic logic : logics) {
                    logic.reset();
                }

                indicatorLed.set(Color.red);
            }

            if(!emergencySystem.shouldStop()){
                hasStopped = false;

                for (Behaviour behaviour : behaviours) {
                    behaviour.process();
                }

                for (Logic logic : logics) {
                    logic.process();
                }
            }
        }
    }
}