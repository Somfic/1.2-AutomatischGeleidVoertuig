package Hardware;

import TI.BoeBot;

public class LineFollower {

    private final int PIN;

    public LineFollower(int pin) {
        this.PIN = pin;
    }

    public int getValue() {
        return BoeBot.analogRead(this.PIN);
    }
}
