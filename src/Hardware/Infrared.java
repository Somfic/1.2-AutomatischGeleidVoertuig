package Hardware;

import TI.BoeBot;
import TI.PinMode;

public class Infrared {
    private final int PIN;

    public Infrared(int pin) {
        this.PIN = pin;
        BoeBot.setMode(pin, PinMode.Input);
    }

    public int getValue(boolean state, int timeout) {
        return BoeBot.pulseIn(this.PIN, state, timeout);
    }
}
