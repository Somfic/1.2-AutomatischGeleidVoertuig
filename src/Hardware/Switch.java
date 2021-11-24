package Hardware;

import TI.BoeBot;
import TI.PinMode;

public class Switch {

    private int pin;

    /**
     * Reads the state of switches, such as buttons and whiskers.
     * @param pin of the breadboard which the switch uses.
     */
    public Switch(int pin){
        this.pin = pin;
    }

    /**
     * Returns the current state of the switch.
     * @return boolean containing the current state of the switch.
     */
    public boolean getState(){
        BoeBot.setMode(this.pin, PinMode.Input);
        return !BoeBot.digitalRead(this.pin);
    }
}
