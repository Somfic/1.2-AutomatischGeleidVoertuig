import Hardware.*;
import Logic.*;
import TI.*;

import java.awt.*;
import java.util.ArrayList;

public class RobotMain {
    static private Switch startButton = new Switch(0);
    static private Switch stopButton = new Switch(1);

    static private Switch leftWhisker = new Switch(8);
    static private Switch rightWhisker = new Switch(10);

    static private BlinkerLogic blinker = new BlinkerLogic();
    static private BuzzerLogic buzzer = new BuzzerLogic();
    static private MotorLogic motor = new MotorLogic(12, 13);

    static private Logic[] logics = {blinker, buzzer, motor};

    static private Led indicatorLed = new Led(1);

    //attributes for autoDriving
    static private boolean turner = false;
    static private boolean toTurn = false;
    static final private int MAXSPEED = 200;

    static private boolean isRunning = false;

    public static void main(String[] args) {

//        buzzer.setIsBuzzing(true);
//        while(true) {
//            BoeBot.wait(1);
//            buzzer.process();
//        }

        while (true) {
            BoeBot.wait(1);
            processLogic();
        }
    }

    /**
     * Drives autonomously until the emergency button is pressed.
     */
    private static void autoDriving() {
        boolean isObstacleLeft = leftWhisker.getState();
        boolean isObstacleRight = rightWhisker.getState();

        motor.start();

        // Blinker logic

        if (turner && motor.targetSpeedReached()) {
            motor.setTimerInterval(10);

            if (toTurn) {
                motor.turn(-MAXSPEED);
                turner = false;
            } else {
                motor.turn(MAXSPEED);
                turner = false;
            }
        } else if (motor.targetSpeedReached()) {

            buzzer.setIsBuzzing(false);
            blinker.setBlinkLeft(false);

            blinker.setBlinkRight(false);

            // No obstacles, happy driving!
            motor.setTargetSpeed(MAXSPEED);
        }

        if (isObstacleLeft && isObstacleRight) {

            System.out.println("! Going backwards");

            buzzer.setIsBuzzing(true);

            // Obstacle on both sides, avoid!
            motor.setTargetSpeed(-MAXSPEED);
        } else if (isObstacleLeft && motor.targetSpeedReached()) {
            System.out.println("! Left");
            blinker.setBlinkRight(true);

            buzzer.setIsBuzzing(true);

            motor.setTimerInterval(5);
            motor.setTargetSpeed(-MAXSPEED);
            turner = true;
            toTurn = false;

        } else if (isObstacleRight && motor.targetSpeedReached()) {

            System.out.println("! Right");

            blinker.setBlinkLeft(true);

            buzzer.setIsBuzzing(true);

            motor.setTimerInterval(5);
            motor.setTargetSpeed(-MAXSPEED);
            turner = true;
            toTurn = true;
        }

        BoeBot.wait(1);
    }

    /**
     * Processes all the logic the class owns.
     */
    private static void processLogic() {
        if (isRunning) {
            autoDriving();

            for (Logic logic : logics) {
                logic.process();
            }

            indicatorLed.set(Color.green);
        } else {
            indicatorLed.set(Color.red);
        }

        if (startButton.getState()) {
            System.out.println("Pressed start button");
            isRunning = true;
        }

        if (stopButton.getState()) {
            System.out.println("Pressed stop button");
            isRunning = false;
            resetLogic();
        }
    }

    /**
     * Resets all the logic the class owns.
     */
    private static void resetLogic() {
        for (Logic logic : logics) {
            logic.reset();
        }
    }
}
