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

    public void setTargetSpeed(int targetSpeed){
        this.leftMotor.setTargetSpeed(1500 + targetSpeed);
        this.rightMotor.setTargetSpeed(1500 - targetSpeed);
    }

    public void setLeftTartgetSpeed(int targetSpeed){
        this.leftMotor.setTargetSpeed(targetSpeed);
    }

    public void setRightTartgetSpeed(int targetSpeed){
        this.rightMotor.setTargetSpeed(targetSpeed);
    }

    public void accelerate() {
        rightMotor.accelerate();
        leftMotor.accelerate();
    }

    public void turn(int direction) {
        setRightTartgetSpeed(1500 - direction);
        setLeftTartgetSpeed(1500 + direction);

        accelerate();

    }
}
