package Hardware;

import TI.Servo;

public class MotorControl {

    private ServoMotor leftMotor;
    private ServoMotor rightMotor;

    public MotorControl(int pinLeftMotor, int pinRightMotor) {
        this.leftMotor = new ServoMotor(pinLeftMotor, false);
        this.rightMotor = new ServoMotor(pinRightMotor, true);
    }

    public void start() {
        this.rightMotor.start();
        this.leftMotor.start();
    }

    public void stop() {
        this.rightMotor.stop();
        this.leftMotor.stop();
    }

    public void setSpeed(int speedChange) {

    }

    public void turn(int direction) {

    }
}
