package Logic;

import Hardware.LineFollower;

public class LineFollowerLogic implements Logic {

    private final LineFollower LINE_FOLLOWER_LEFT;
    private final LineFollower LINE_FOLLOWER_CENTER;
    private final LineFollower LINE_FOLLOWER_RIGHT;

    private boolean stateLeft = false;
    private boolean stateCenter = true;
    private boolean stateRight = false;

    public LineFollowerLogic(int pinLeft, int pinCenter, int pinRight) {
        this.LINE_FOLLOWER_LEFT = new LineFollower(pinLeft);
        this.LINE_FOLLOWER_CENTER = new LineFollower(pinCenter);
        this.LINE_FOLLOWER_RIGHT = new LineFollower(pinRight);
    }

    public boolean getStateLeft(){
        return this.stateLeft;
    }

    public boolean getStateCenter() {
        return this.stateCenter;
    }

    public boolean getStateRight(){
        return this.stateRight;
    }

    @Override
    public void process() {
        this.stateLeft = this.LINE_FOLLOWER_LEFT.getValue() < 800;
        this.stateCenter = this.LINE_FOLLOWER_CENTER.getValue() > 800;
        this.stateRight = this.LINE_FOLLOWER_RIGHT.getValue() < 800;
    }

    @Override
    public void reset() {
        this.stateLeft = false;
        this.stateRight = false;
        this.stateCenter = false;
    }
}
