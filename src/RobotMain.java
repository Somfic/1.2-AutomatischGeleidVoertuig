import Hardware.*;
import Logic.*;
import TI.*;

import System.*;

import java.awt.*;

public class RobotMain {
    static private Switch startButton = new Switch(1);
    static private Switch stopButton = new Switch(0);

    static private LedLogic lights = new LedLogic();
    static private BuzzerLogic buzzer = new BuzzerLogic();
    static private MotorLogic motors = new MotorLogic(12, 13);

    static private MovementSystem movementSystem = new MovementSystem(motors);
    static private BlinkerSystem blinkerSystem = new BlinkerSystem(lights, motors);

    static private Logic[] logics = {lights, buzzer, motors};
    static private SystemInterface[] systems = {movementSystem, blinkerSystem};

    static private EmergencySystem emergencySystem = new EmergencySystem();

    static private Led indicatorLed = new Led(1);

    static private boolean hasStopped;

    public static void main(String[] args) {

        for (SystemInterface system : systems) {
            system.initialise();
        }

        emergencySystem.initialise();

        while (true) {
            BoeBot.wait(1);

            emergencySystem.process();

            if(emergencySystem.shouldStop() && !hasStopped) {
                hasStopped = true;

                for (SystemInterface system : systems) {
                    system.reset();
                }

                for (Logic logic : logics) {
                    logic.reset();
                }

                indicatorLed.set(Color.red);
            }

            if(!emergencySystem.shouldStop()){
                hasStopped = false;

                for (SystemInterface system : systems) {
                    system.process();
                }

                for (Logic logic : logics) {
                    logic.process();
                }
            }
        }
    }
}