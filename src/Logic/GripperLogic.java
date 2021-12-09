package Logic;

import Hardware.ServoMotor;
import TI.Timer;

public class GripperLogic implements Logic {
    private final int DISTANCE_TO_ENDS = 200;

    private final int DEFAULT_SPEED = 1500;
    private final int MAX_SPEED = 200;
    private final int ACCELERATION = 1;
    private final int DETERMINED_SPEED = 50;
    private final Timer intervalTimer = new Timer(100);

    private int state = 0;

    private int currentSpeed = 1500;

    private ServoMotor gripper;

    public GripperLogic(int pin){
        this.gripper = new ServoMotor(pin);
        this.gripper.start();
    }

    public void close(){
        this.state = -1;
    }

    public void open(){
        this.state = 1;
    }

    public void process(){
        if (this.intervalTimer.timeout()) {
            if (this.state == 1) {
                if (currentSpeed < DEFAULT_SPEED + DETERMINED_SPEED) {
                    currentSpeed += ACCELERATION;
                    this.gripper.set(currentSpeed);
                } else {
                    state = 0;
                }
            } else if (state == -1) {
                if (currentSpeed > DEFAULT_SPEED - DETERMINED_SPEED) {
                    currentSpeed -= ACCELERATION;
                    this.gripper.set(currentSpeed);
                } else {
                    state = 0;
                }
            } else {
                currentSpeed = DEFAULT_SPEED;
                this.gripper.set(currentSpeed);
            }
        }
    }

    public void reset(){
        open();
    }

}
