package Logic;

import Hardware.LineFollower;

public class LineFollowerLogic implements Logic {

    private final LineFollower lineFollowerLeft;
    private final LineFollower lineFollowerMiddle;
    private final LineFollower lineFollowerRight;

    public LineFollowerLogic(int pinLeft, int pinCenter, int pinRight) {
        this.lineFollowerLeft = new LineFollower(pinLeft);
        this.lineFollowerMiddle = new LineFollower(pinCenter);
        this.lineFollowerRight = new LineFollower(pinRight);
    }

    @Override
    public void process() {
       // int this.lineFollowerRight.getValue();
    }

    @Override
    public void reset() {

    }
}
