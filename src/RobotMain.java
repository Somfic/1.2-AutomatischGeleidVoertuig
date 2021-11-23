import Hardware.*;
import Logic.*;
import TI.*;

public class RobotMain {

    public static void main(String[] args) {

        Switch leftWhisker = new Switch(8);
        Switch rightWhisker = new Switch(10);

        Led frontLeftLed = new Led(3);
        Led frontRightLed = new Led(5);
        Led backLeftLed = new Led(2);
        Led backRightLed = new Led(0);

        BlinkerLogic blinker = new BlinkerLogic(frontLeftLed, frontRightLed, backLeftLed, backRightLed);

        MotorLogic motor = new MotorLogic(12, 13);

        BuzzerLogic buzzer = new BuzzerLogic(new Buzzer(0));

        while(true) {
            BoeBot.wait(1);

            boolean isObstacleLeft = !leftWhisker.getState();
            boolean isObstacleRight = !rightWhisker.getState();

            // Blinker logic
            blinker.setBlinkLeft(isObstacleLeft);
            blinker.setBlinkRight(isObstacleRight);

            if(isObstacleLeft && isObstacleRight) {

                System.out.println("! Going backwards");

                // Obstacle on both sides, avoid!
                motor.setTargetSpeed(-200);
            }

            else if(isObstacleLeft)
            {
                System.out.println("! Left");

                // Obstacle on the left, avoid!
                motor.turn(200);
            }

            else if(isObstacleRight) {

                System.out.println("! Right");

                // Obstacle on the right, avoid!
                motor.turn(-200);
            }

            else {

                // No obstacles, happy driving!
                motor.setTargetSpeed(200);
            }

            motor.process();
            blinker.process();
        }
    }
}
