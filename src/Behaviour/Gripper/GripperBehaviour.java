package Behaviour.Gripper;

import Behaviour.Behaviour;
import Logic.GripperLogic;
import Logic.InfraredLogic;

public class GripperBehaviour implements Behaviour {

    private final GripperLogic GRIPPER_LOGIC;
    private final int OPENING_STATE = 1;
    private final int CLOSING_STATE = -1;

    private final InfraredLogic INFRARED_LOGIC;

    //TODO: change these codes to the correct ones
    private final int OPENING_CODE = 184;
    private final int CLOSING_CODE = 191;

    public GripperBehaviour(GripperLogic gripperLogic, InfraredLogic infraredLogic){
        this.GRIPPER_LOGIC = gripperLogic;
        this.INFRARED_LOGIC = infraredLogic;
    }

    public void open(){
        this.GRIPPER_LOGIC.setState(OPENING_STATE);
    }

    public void close() {
        this.GRIPPER_LOGIC.setState(CLOSING_STATE);
    }

    public void initialise(){

    }

    public void process(){
        if (INFRARED_LOGIC.getLastCode() == OPENING_CODE){
            open();
        } else if (INFRARED_LOGIC.getLastCode() == CLOSING_CODE){
            close();
        }
    }

    public void reset(){

    }
}
