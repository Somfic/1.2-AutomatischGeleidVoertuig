package Behaviour.Distance;

import Behaviour.Behaviour;
import Logger.Logger;
import Logic.DistanceLogic;
import Logic.GripperLogic;

public class DistanceBehaviour implements Behaviour {

    private final Logger LOGGER = new Logger(this);

    private final DistanceLogic LOWER_DISTANCE;
    private final DistanceLogic UPPER_DISTANCE;

    private int currentSonar = 0;
    private final int LOWER_NUMBER = 0;
    private final int UPPER_NUMBER = 1;

    private final GripperLogic GRIPPERS;

    private int lastDistance = 0;

    public DistanceBehaviour(DistanceLogic distance, DistanceLogic upperDistance, GripperLogic gripperLogic) {
        this.LOWER_DISTANCE = distance;
        this.UPPER_DISTANCE = upperDistance;

        this.GRIPPERS = gripperLogic;
    }

    public float getPulse(){
        if (currentSonar == LOWER_NUMBER){
            return LOWER_DISTANCE.getPulse();
        } else {
            return UPPER_DISTANCE.getPulse();
        }
    }

    public float getDistance(){
        if (currentSonar == LOWER_NUMBER){
            return LOWER_DISTANCE.getDistance();
        } else {
            return UPPER_DISTANCE.getDistance();
        }
    }

    @Override
    public void initialise() {

    }

    @Override
    public void process() {
        if (this.GRIPPERS.isClosed()){
            currentSonar = UPPER_NUMBER;
        } else {
            currentSonar = LOWER_NUMBER;
        }

        int measuredDistance = Math.round(getDistance());
        if (measuredDistance != this.lastDistance) {
            this.lastDistance = measuredDistance;
        }

    }

    @Override
    public void reset() {

    }
}
