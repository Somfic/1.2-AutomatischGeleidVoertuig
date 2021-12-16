package Logic;

import Hardware.ServoMotor;
import TI.Timer;

public class GripperLogic implements Logic {

    private final int SPEED = 10;
    private final Timer INTERVAL_TIMER = new Timer(6);

    private final int OPENED_DISTANCE = 1900;
    private final int CLOSED_DISTANCE = 1025;

    private final int DEFAULT_STATE = 0;
    private final int CLOSING_STATE = -1;
    private final int OPENING_STATE = 1;
    private int state = DEFAULT_STATE;

    private int currentPlace = OPENED_DISTANCE;

    private ServoMotor gripper;

    public GripperLogic(int pin) {
        this.gripper = new ServoMotor(pin);
        this.gripper.start();
        this.gripper.set(OPENED_DISTANCE);
    }

    public void setState(int state){
        this.state = state;
    }

    public void process(){
        if (this.INTERVAL_TIMER.timeout()) {
            if (this.state == OPENING_STATE) {
                if (currentPlace < OPENED_DISTANCE) {
                    currentPlace += SPEED;
                    this.gripper.set(currentPlace);
                } else {
                    state = DEFAULT_STATE;
                }
            } else if (state == CLOSING_STATE) {
                if (currentPlace > CLOSED_DISTANCE) {
                    currentPlace -= SPEED;
                    this.gripper.set(currentPlace);
                } else {
                     state = DEFAULT_STATE;
                }
            }
        }
    }

    public void reset() {
        this.gripper.set(OPENED_DISTANCE);
    }

}
