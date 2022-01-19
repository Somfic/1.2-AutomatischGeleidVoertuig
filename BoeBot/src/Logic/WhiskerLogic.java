package Logic;

import Hardware.Switch;

public class WhiskerLogic implements Logic {

    private final Switch LEFT_WHISKER;
    private final Switch RIGHT_WHISKER;

    private boolean hasObstacleLeft;
    private boolean hasObstacleRight;

    public WhiskerLogic(int pinLeft, int pinRight) {
        this.LEFT_WHISKER = new Switch(pinLeft);
        this.RIGHT_WHISKER = new Switch(pinRight);
    }

    public boolean hasObstacleLeft() {
        return this.hasObstacleLeft;
    }

    public boolean hasObstacleRight() {
        return this.hasObstacleRight;
    }

    @Override
    public void process() {
        this.hasObstacleLeft = this.LEFT_WHISKER.getState();
        this.hasObstacleRight = this.RIGHT_WHISKER.getState();
    }

    @Override
    public void reset() {

    }
}
