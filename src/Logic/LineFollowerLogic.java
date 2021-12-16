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
        if (this.LINE_FOLLOWER_LEFT.getValue() < 800){
            this.stateLeft = true;
        } else {
            this.stateLeft = false;
        }
        return this.stateLeft;
    }

    public boolean getStateCenter(){
        if (this.LINE_FOLLOWER_CENTER.getValue() > 800){
            this.stateCenter = true;
        } else {
            this.stateCenter = false;
        }
        return this.stateCenter;
    }

    public boolean getStateRight(){
        if (this.LINE_FOLLOWER_RIGHT.getValue() < 800){
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
