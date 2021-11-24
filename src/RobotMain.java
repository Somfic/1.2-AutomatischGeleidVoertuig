import Hardware.*;
import Logic.*;
import TI.*;

import java.util.ArrayList;

public class RobotMain {
    static private Switch button1 = new Switch(0);
    static private Switch button2 = new Switch(1);

    static private Switch leftWhisker = new Switch(2);
    static private Switch rightWhisker = new Switch(3);

    static private BlinkerLogic blinker = new BlinkerLogic(new Led(3), new Led(5), new Led(2), new Led(0));
    static private BuzzerLogic buzzer = new BuzzerLogic(new Buzzer(4));
    static private MotorLogic motor = new MotorLogic(13, 12);

    static private Logic[] logics = {blinker, buzzer, motor};

    //attributes for autoDriving
    static private boolean turner = false;
    static private boolean toTurn = false;
    static final private int MAXSPEED = 200;



    public static void main(String[] args) {

        while (true) {
            BoeBot.wait(1);

            processLogic();

            if (button1.getState()) {
                autoDriving();
            }
        }
    }
    private static void autoDriving(){
        while (button2.getState()) {

            processLogic();

            boolean isObstacleLeft = !leftWhisker.getState();
            boolean isObstacleRight = !rightWhisker.getState();

            // Blinker logic
            blinker.setBlinkLeft(isObstacleLeft);
            blinker.setBlinkRight(isObstacleRight);

            if (turner && motor.targetSpeedReached()) {
                System.out.println("arrived");

                motor.setTimerInterval(10);

                if (toTurn) {
                    motor.turn(-MAXSPEED);
                    turner = false;
                } else {
                    motor.turn(MAXSPEED);
                    turner = false;
                }
            } else if (motor.targetSpeedReached()) {

                // No obstacles, happy driving!
                motor.setTargetSpeed(MAXSPEED);
            }

            if (isObstacleLeft && isObstacleRight) {

                System.out.println("! Going backwards");

                // Obstacle on both sides, avoid!
                motor.setTargetSpeed(-MAXSPEED);
            } else if (isObstacleLeft && motor.targetSpeedReached()) {
                System.out.println("! Left");

                motor.setTimerInterval(5);
                motor.setTargetSpeed(-MAXSPEED);
                turner = true;
                toTurn = false;

            } else if (isObstacleRight && motor.targetSpeedReached()) {

                System.out.println("! Right");

                motor.setTimerInterval(5);
                motor.setTargetSpeed(-MAXSPEED);
                turner = true;
                toTurn = true;
            }

            BoeBot.wait(1);
        }
        resetLogic();
    }
    private static void processLogic(){
        for (Logic logic : logics) {
            logic.process();
        }
    }

    private static void resetLogic(){
        for (Logic logic : logics) {
            logic.reset();
        }
    }
}
