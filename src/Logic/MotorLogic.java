package Logic;

import Hardware.ServoMotor;
import TI.Servo;
import TI.Timer;
import sun.rmi.runtime.Log;

public class MotorLogic implements Logic {

    private ServoMotor leftMotor;
    private ServoMotor rightMotor;

    private Timer timer = new Timer(100);

    public MotorLogic(int pinLeftMotor, int pinRightMotor) {
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

    public void setLeftTargetSpeed(int targetSpeed){
        this.leftMotor.setTargetSpeed(targetSpeed);
    }

    public void setRightTargetSpeed(int targetSpeed){
        this.rightMotor.setTargetSpeed(targetSpeed);
    }

    public void turn(int direction) {
        setRightTargetSpeed(1500 + direction);
        setLeftTargetSpeed(1500 + direction);
    }

    @Override
    public void process() {
        if(timer.timeout()) {
            timer.mark();

            rightMotor.accelerate();
            leftMotor.accelerate();
        }
    }
}
