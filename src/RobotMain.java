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

        MotorLogic motor = new MotorLogic(13, 12);

        BuzzerLogic buzzer = new BuzzerLogic(new Buzzer(0));

        Timer timeout = new Timer(1000);

        boolean turner = false;

        boolean toTurn = false;

        while(true) {
            BoeBot.wait(1);

            motor.process();
            blinker.process();

            boolean isObstacleLeft = !leftWhisker.getState();
            boolean isObstacleRight = !rightWhisker.getState();

            // Blinker logic
            blinker.setBlinkLeft(isObstacleLeft);
            blinker.setBlinkRight(isObstacleRight);

            if(turner && motor.targetSpeedReached()) {
                System.out.println("arrived");

                motor.setTimerInterval(10);

                if(toTurn){
                    motor.turn(-200);
                    turner = false;
                }
                else{
                    motor.turn(200);
                    turner = false;
                }
            }

            else if(motor.targetSpeedReached()){

                // No obstacles, happy driving!
                motor.setTargetSpeed(200);
            }

            if(isObstacleLeft && isObstacleRight) {

                System.out.println("! Going backwards");

                // Obstacle on both sides, avoid!
                motor.setTargetSpeed(-200);
            }

            else if(isObstacleLeft && motor.targetSpeedReached())
            {
                System.out.println("! Left");

                motor.setTimerInterval(5);
                motor.setTargetSpeed(-200);
                turner = true;
                toTurn = false;

            }

            else if(isObstacleRight && motor.targetSpeedReached()) {

                System.out.println("! Right");

                motor.setTimerInterval(5);
                motor.setTargetSpeed(-200);
                turner = true;
                toTurn = true;
            }






        }
    }
}
