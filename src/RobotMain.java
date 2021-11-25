import Hardware.*;
import Logic.*;
import TI.*;

import java.awt.*;
import java.util.Random;

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
    static private boolean turningToRight = false;

    static final private int MAXSPEED = 100;
    static final private int TURNSPEED = 130;

    static private boolean isRunning = false;

    public static void main(String[] args) {

        while (true) {
            BoeBot.wait(1);
            processLogic();
        }
    }

    /**
     * Drives autonomously until the emergency button is pressed.
     */
    private static void autoDriving() {

        // Obstacle detection
        boolean isObstacleLeft = leftWhisker.getState();
        boolean isObstacleRight = rightWhisker.getState();

        motor.start();

        // Blinker logic

        if (turner && motor.targetSpeedReached()) {

            System.out.println("Starting turn");

            motor.setTimerInterval(10);

            if (turningToRight) {
                motor.turn(-TURNSPEED);
                turner = false;
            } else {
                motor.turn(TURNSPEED);
                turner = false;
            }
        } 
        
        else if (motor.targetSpeedReached()) {

            System.out.println("Going forwards");

            buzzer.setIsBuzzing(false);
            blinker.setBlinkLeft(false);

            blinker.setBlinkRight(false);

            // No obstacles, happy driving!
            motor.setTimerInterval(1);
            motor.setTargetSpeed(MAXSPEED);
        }

        // If there's an obstacle on both sides
        if (isObstacleLeft && isObstacleRight && motor.targetSpeedReached()) {

            System.out.println("Obstacle on both sides");

            // Start buzzing
            buzzer.setIsBuzzing(true);

            // Go backwards
            motor.setTimerInterval(10);
            motor.stop();
            motor.start();
            motor.setTargetSpeed(-MAXSPEED);

            turner = true;
            turningToRight = Math.random() < 0.5f;
        }
        
        // If there's an obstacle on the left
        else if (isObstacleLeft && motor.targetSpeedReached()) {

            System.out.println("Obstacle on left");

            // Start blinking
            blinker.setBlinkRight(true);

            // Start buzzing
            buzzer.setIsBuzzing(true);

            // Go backwards
            motor.setTimerInterval(10);
            motor.stop();
            motor.start();
            motor.setTargetSpeed(-MAXSPEED);

            // Mark the turner to turn to the right
            turner = true;
            turningToRight = false;

        } 
        
        // If there's an obstacle on the right
        else if (isObstacleRight && motor.targetSpeedReached()) {

            System.out.println("Obstacle on right");

            // Start blinking
            blinker.setBlinkLeft(true);

            // Start buzzing
            buzzer.setIsBuzzing(true);

            // Go backwards
            motor.setTimerInterval(10);
            motor.stop();
            motor.start();
            motor.setTargetSpeed(-MAXSPEED);

            // Mark the turner to turn to the left
            turner = true;
            turningToRight = true;
        }
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

        // Start button logic
        if (startButton.getState()) {
            System.out.println("Pressed start button");

            isRunning = true;
        }

        // (Emergency) stop button logic
        if (stopButton.getState()) {
            System.out.println("Pressed stop button");

            isRunning = false;

            // Reset the logic
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
