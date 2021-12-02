import Hardware.*;
import Logic.*;
import TI.*;

import Behaviour.*;

import java.awt.*;

public class RobotMain {
    static private Switch startButton = new Switch(1);
    static private Switch stopButton = new Switch(0);

    static private LedLogic lights = new LedLogic();
    static private BuzzerLogic buzzer = new BuzzerLogic(2);
    static private MotorLogic motors = new MotorLogic(12, 13);
    static private InfraredLogic infrared = new InfraredLogic(3);

    static private MovementBehaviour movementBehaviour = new MovementBehaviour(motors, infrared);
    static private BlinkerBehaviour blinkerBehaviour = new BlinkerBehaviour(lights, motors);
    static private RemoteBehaviour remoteBehaviour = new RemoteBehaviour(infrared);
    static private BuzzerBehaviour buzzerBehaviour = new BuzzerBehaviour(buzzer, infrared, motors);

    static private Logic[] logics = {lights, buzzer, motors, infrared};
    static private Behaviour[] behaviours = {movementBehaviour, blinkerBehaviour, remoteBehaviour, buzzerBehaviour};

    static private EmergencyBehaviour emergencySystem = new EmergencyBehaviour(infrared);

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

                //System.out.println("Emergency stop! Resetting");

                for (Behaviour behaviour : behaviours) {
                    behaviour.reset();
                }

                for (Logic logic : logics) {
                    logic.reset();
                }

                indicatorLed.set(Color.red);
            }

            if(!emergencySystem.shouldStop()){
                if(hasStopped) {
                    //System.out.println("Starting again! Resetting");

                    for (Behaviour behaviour : behaviours) {
                        behaviour.reset();
                    }

                    for (Logic logic : logics) {
                        logic.reset();
                    }
                }

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