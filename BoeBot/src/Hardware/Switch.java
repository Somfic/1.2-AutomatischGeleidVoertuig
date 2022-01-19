package Hardware;

import TI.BoeBot;
import TI.PinMode;

public class Switch {

    private final int PIN;

    /**
     * Reads the state of switches, such as buttons and whiskers.
     *
     * @param pin of the breadboard which the switch uses.
     */
    public Switch(int pin) {
        this.PIN = pin;
        BoeBot.setMode(this.PIN, PinMode.Input);
    }

    /**
     * Returns the current state of the switch.
     *
     * @return boolean containing the current state of the switch.
     */
    public boolean getState() {
        return !BoeBot.digitalRead(this.PIN);
    }
}
