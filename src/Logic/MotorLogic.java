package Logic;

import Hardware.ServoMotor;
import TI.Servo;
import TI.Timer;

public class MotorLogic implements Logic {

    private ServoMotor leftMotor;
    private ServoMotor rightMotor;

    private Timer timer = new Timer(10);
//    private Timer timeoutTimer = new Timer(1000);

    final private int DEFAULTSPEED = 1500;

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

    public void setTimerInterval(int interval) {
        this.timer.setInterval(interval);
    }

    public void setTargetSpeed(int targetSpeed){
//        if(timeoutTimer.timeout()) {
//            timeoutTimer.mark();
            this.leftMotor.setTargetSpeed(DEFAULTSPEED + targetSpeed);
            this.rightMotor.setTargetSpeed(DEFAULTSPEED - targetSpeed);
//        }
    }

    public void setLeftTargetSpeed(int targetSpeed){
        this.leftMotor.setTargetSpeed(targetSpeed);
    }

    public void setRightTargetSpeed(int targetSpeed){
        this.rightMotor.setTargetSpeed(targetSpeed);
    }

    public void turn(int direction) {
        setRightTargetSpeed(DEFAULTSPEED + direction);
        setLeftTargetSpeed(DEFAULTSPEED + direction);
    }

    public boolean targetSpeedReached(){
        if(this.rightMotor.targetSpeedReached() && this.leftMotor.targetSpeedReached()){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public void process() {
        if(timer.timeout()) {
            timer.mark();

            rightMotor.accelerate();
            leftMotor.accelerate();
        }
    }

    @Override
    public void reset(){
        setTargetSpeed(DEFAULTSPEED);
        stop();
    }
}
