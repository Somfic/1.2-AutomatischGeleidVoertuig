package Logic;

import Hardware.LineFollower;

public class LineFollowerLogic implements Logic {

    private final LineFollower LINE_FOLLOWER_LEFT;
    private final LineFollower LINE_FOLLOWER_MIDDLE;
    private final LineFollower LINE_FOLLOWER_RIGHT;
    private boolean stateLeft = false;
    private boolean stateCenter = true;
    private boolean stateRight = false;

    public LineFollowerLogic(int pinLeft, int pinCenter, int pinRight) {
        this.LINE_FOLLOWER_LEFT = new LineFollower(pinLeft);
        this.LINE_FOLLOWER_MIDDLE = new LineFollower(pinCenter);
        this.LINE_FOLLOWER_RIGHT = new LineFollower(pinRight);
    }

    public boolean getStateLeft(){
        if (this.lineFollowerLeft.getValue() < 800){
            this.stateLeft = true;
        } else {
            this.stateLeft = false;
        }
        return this.stateLeft;
    }

    public boolean getStateCenter(){
        if (this.lineFollowerCenter.getValue() < 800){
            this.stateCenter = false;
        } else {
            this.stateCenter = true;
        }
        return this.stateCenter;
    }

    public boolean getStateRight(){
        if (this.lineFollowerRight.getValue() < 800){
            this.stateRight = true;
        } else {
            this.stateRight = false;
        }
        return this.stateRight;
    }

    @Override
    public void process() {
        getStateCenter();
        getStateLeft();
        getStateRight();
    }

    @Override
    public void reset() {

    }
}
