package Logic;

import Hardware.ServoMotor;
import TI.Servo;
import TI.Timer;

public class MotorLogic implements Logic {

    private ServoMotor leftMotor;
    private ServoMotor rightMotor;

    private Timer timer;

    final private int STATIONAIRY_SPEED = 1500;
    final private int MAX_SPEED = 100;

    private float targetSpeed = 0;
    private float targetAngle = 0;

    private int leftPulse = 0;
    private int leftTargetPulse = 0;

    private int rightPulse = 0;
    private int rightTargetPulse = 0;

    private float acceleration = 10;

    public MotorLogic(int pinLeftMotor, int pinRightMotor) {
        this.leftMotor = new ServoMotor(pinLeftMotor);
        this.rightMotor = new ServoMotor(pinRightMotor);

        this.timer = new Timer(100);
    }

    public void start() {
        this.rightMotor.start();
        this.leftMotor.start();
    }

    public void stop() {
        leftPulse = 0;
        rightPulse = 0;
        leftTargetPulse = 0;
        rightTargetPulse = 0;

        this.rightMotor.stop();
        this.leftMotor.stop();
    }

    public float getTargetSpeed() {
        return this.targetSpeed;
    }

    public float getTargetAngle() {
        return this.targetAngle;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public void setMove(float speed, float angle) {
        this.targetSpeed = speed;
        this.targetAngle = angle;

        float deltaSpeedPulse = 0.5f * MAX_SPEED * speed;
        float deltaAnglePulse = 0.5f * MAX_SPEED * angle;

        leftTargetPulse = Math.round(deltaSpeedPulse + deltaAnglePulse);
        rightTargetPulse = Math.round(deltaSpeedPulse - deltaAnglePulse);
    }

    @Override
    public void process() {
        if (timer.timeout()) {
            timer.mark();

            // Calculate the left pulse based on the acceleration
            int leftDelta = leftTargetPulse - leftPulse;

            if (leftDelta < 0) {
                // Going backwards
                leftPulse -= Math.min(Math.abs(leftDelta), acceleration);
            } else {
                // Going forwards
                leftPulse += Math.min(leftDelta, acceleration);
            }

            // Calculate the right pulse based on the acceleration
            int rightDelta = rightTargetPulse - rightPulse;

            if (rightDelta < 0) {
                // Going backwards
                rightPulse -= Math.min(Math.abs(rightDelta), acceleration);
            } else {
                // Going forwards
                rightPulse += Math.min(rightDelta, acceleration);
            }

            rightMotor.set(STATIONAIRY_SPEED - leftPulse);
            leftMotor.set(STATIONAIRY_SPEED + rightPulse);
        }
    }



    @Override
    public void reset() {
        stop();
        this.targetAngle = 0;
        this.targetSpeed = 0;
        this.acceleration = 5;
    }
}
