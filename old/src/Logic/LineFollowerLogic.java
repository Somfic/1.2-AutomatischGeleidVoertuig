package Logic;

import Hardware.LineFollower;

import java.util.ArrayList;

public class LineFollowerLogic implements Logic {

    private final LineFollower LINE_FOLLOWER_LEFT;
    private final LineFollower LINE_FOLLOWER_CENTER;
    private final LineFollower LINE_FOLLOWER_RIGHT;

    private final ArrayList<LineFollower> lineFollowers = new ArrayList<>();

    private boolean stateLeft = false;
    private boolean stateCenter = true;
    private boolean stateRight = false;

    private double treshold = 800.0;

    public LineFollowerLogic(int pinLeft, int pinCenter, int pinRight) {
        this.LINE_FOLLOWER_LEFT = new LineFollower(pinLeft);
        this.LINE_FOLLOWER_CENTER = new LineFollower(pinCenter);
        this.LINE_FOLLOWER_RIGHT = new LineFollower(pinRight);

        lineFollowers.add(this.LINE_FOLLOWER_CENTER);
        lineFollowers.add(this.LINE_FOLLOWER_RIGHT);
        lineFollowers.add(this.LINE_FOLLOWER_LEFT);
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

    public void calibrate(){
        int highest = LINE_FOLLOWER_LEFT.getValue();
        int lowest = LINE_FOLLOWER_LEFT.getValue();

        for (LineFollower lf : this.lineFollowers){
            if (highest < lf.getValue()){
                highest = lf.getValue();
            } else if (lowest > lf.getValue()){
                lowest = lf.getValue();
            }
        }

        int delta = highest - lowest;

        this.treshold = lowest + delta / 2.0;

    }

    @Override
    public void process() {
        this.stateLeft = this.LINE_FOLLOWER_LEFT.getValue() < this.treshold;
        this.stateCenter = this.LINE_FOLLOWER_CENTER.getValue() > this.treshold;
        this.stateRight = this.LINE_FOLLOWER_RIGHT.getValue() < this.treshold;
    }

    @Override
    public void reset() {
        this.stateLeft = false;
        this.stateRight = false;
        this.stateCenter = false;
    }
}
