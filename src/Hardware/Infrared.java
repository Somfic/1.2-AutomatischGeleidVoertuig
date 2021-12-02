package Hardware;

import TI.BoeBot;
import TI.PinMode;

public class Infrared {
    private int pin;

    public Infrared(int pin) {
        this.pin = pin;
        BoeBot.setMode(pin, PinMode.Input);
    }

    public int getValue(boolean state, int timeout) {
        return BoeBot.pulseIn(this.pin, state, timeout);
    }
}
