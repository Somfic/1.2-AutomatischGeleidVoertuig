package Logic;

import Hardware.LineFollower;

public class LineFollowerLogic implements Logic {

    private final LineFollower LINE_FOLLOWER_LEFT;
    private final LineFollower LINE_FOLLOWER_MIDDLE;
    private final LineFollower LINE_FOLLOWER_RIGHT;

    public LineFollowerLogic(int pinLeft, int pinCenter, int pinRight) {
        this.LINE_FOLLOWER_LEFT = new LineFollower(pinLeft);
        this.LINE_FOLLOWER_MIDDLE = new LineFollower(pinCenter);
        this.LINE_FOLLOWER_RIGHT = new LineFollower(pinRight);
    }

    @Override
    public void process() {

    }

    @Override
    public void reset() {

    }
}
