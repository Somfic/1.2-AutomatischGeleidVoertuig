package Logic;

import Hardware.ServoMotor;
import TI.Timer;

public class GripperLogic implements Logic {

    private final int SPEED = 1;
    private final Timer INTERVAL_TIMER = new Timer(6);

    private final int OPENED_DISTANCE = 1900;
    private final int CLOSED_DISTANCE = 1025;

    private int state = 0;

    private int currentPlace = OPENED_DISTANCE;

    private ServoMotor gripper;

    public GripperLogic(int pin) {
        this.gripper = new ServoMotor(pin);
        this.gripper.start();
        this.gripper.set(OPENED_DISTANCE);
    }

    public void close() {
        this.state = -1;
    }

    public void open() {
        this.state = 1;
    }

    public void process(){
        if (this.INTERVAL_TIMER.timeout()) {
            if (this.state == 1) {
                if (currentPlace < OPENED_DISTANCE) {
                    currentPlace += SPEED;
                    this.gripper.set(currentPlace);
                } else {
                    state = 0;
                }
            } else if (state == -1) {
                if (currentPlace > CLOSED_DISTANCE) {
                    currentPlace -= SPEED;
                    this.gripper.set(currentPlace);
                } else {
                    state = 0;
                }
            }
        }
    }

    public void reset() {
        this.gripper.set(OPENED_DISTANCE);
    }

}
