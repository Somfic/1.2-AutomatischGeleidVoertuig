import Hardware.*;
import Logic.*;
import TI.*;

import java.util.ArrayList;

public class RobotMain {
    private Switch button1 = new Switch(0);
    private Switch button2 = new Switch(1);

    private Switch leftWhisker = new Switch(2);
    private Switch rightWhisker = new Switch(3);

    private BlinkerLogic blinker = new BlinkerLogic(new Led(3), new Led(5), new Led(2), new Led(0));
    private BuzzerLogic buzzer = new BuzzerLogic(new Buzzer(4));
    private MotorLogic motor = new MotorLogic(13, 12);

    private Logic[] logics = {blinker, buzzer, motor};

    //attributes for autoDriving
    private boolean turner = false;
    private boolean toTurn = false;
    final private int DEFAULTSPEED = 1500;



    public void main(String[] args) {

        while (true) {
            BoeBot.wait(1);

            processLogic();

            if (button1.getState()) {
                autoDriving();
            }
        }
    }
    private void autoDriving(){
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
                    motor.turn(-DEFAULTSPEED);
                    turner = false;
                } else {
                    motor.turn(DEFAULTSPEED);
                    turner = false;
                }
            } else if (motor.targetSpeedReached()) {

                // No obstacles, happy driving!
                motor.setTargetSpeed(DEFAULTSPEED);
            }

            if (isObstacleLeft && isObstacleRight) {

                System.out.println("! Going backwards");

                // Obstacle on both sides, avoid!
                motor.setTargetSpeed(-DEFAULTSPEED);
            } else if (isObstacleLeft && motor.targetSpeedReached()) {
                System.out.println("! Left");

                motor.setTimerInterval(5);
                motor.setTargetSpeed(-DEFAULTSPEED);
                turner = true;
                toTurn = false;

            } else if (isObstacleRight && motor.targetSpeedReached()) {

                System.out.println("! Right");

                motor.setTimerInterval(5);
                motor.setTargetSpeed(-DEFAULTSPEED);
                turner = true;
                toTurn = true;
            }

            BoeBot.wait(1);
        }
        for (Logic logic : this.logics) {
            logic.reset();
        }
    }
    private void processLogic(){
        for (Logic logic : logics) {
            logic.process();
        }
    }
}
