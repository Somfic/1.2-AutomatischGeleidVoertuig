package Hardware;

import TI.BoeBot;

public class LineFollower {

    private final int pin;

    public LineFollower(int pin) {
        this.pin = pin;
    }

    public int getValue() {
        return BoeBot.analogRead(this.pin);
    }
}
