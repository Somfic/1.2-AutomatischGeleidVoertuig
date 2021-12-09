package Behaviour.Distance;

import Behaviour.Behaviour;
import Logger.Logger;
import Logic.DistanceLogic;

public class DistanceBehaviour implements Behaviour {

    private final Logger logger = new Logger(this);

    private final DistanceLogic distance;

    private float lastDistance = 0;

    public DistanceBehaviour(DistanceLogic distance) {

        this.distance = distance;
    }

    @Override
    public void initialise() {

    }

    @Override
    public void process() {
        float measuredDistance = this.distance.getDistance();

        if (measuredDistance != this.lastDistance) {
            this.lastDistance = measuredDistance;

            logger.info(Math.round(measuredDistance) + " cm");
        }
    }

    @Override
    public void reset() {

    }
}
