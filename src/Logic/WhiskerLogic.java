package Logic;

import Hardware.Switch;

public class WhiskerLogic implements Logic {

    private final Switch leftWhisker;
    private final Switch rightWhisker;

    private boolean hasObstacleLeft;
    private boolean hasObstacleRight;

    public WhiskerLogic(int pinLeft, int pinRight) {
        this.leftWhisker = new Switch(pinLeft);
        this.rightWhisker = new Switch(pinRight);
    }

    public boolean hasObstacleLeft() {
        return this.hasObstacleLeft;
    }

    public boolean hasObstacleRight() {
        return this.hasObstacleRight;
    }

    @Override
    public void process() {
        this.hasObstacleLeft = this.leftWhisker.getState();
        this.hasObstacleRight = this.rightWhisker.getState();
    }

    @Override
    public void reset() {

    }
}
