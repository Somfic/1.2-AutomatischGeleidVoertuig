package Hardware;

import TI.BoeBot;
import TI.PinMode;

public class Ultrasonic {

    private final int TRIGGER_PIN;
    private final int ECHO_PIN;

    public Ultrasonic(int triggerPin, int echoPin) {
        this.TRIGGER_PIN = triggerPin;
        this.ECHO_PIN = echoPin;

        BoeBot.setMode(this.TRIGGER_PIN, PinMode.Output);
        BoeBot.setMode(this.ECHO_PIN, PinMode.Input);
    }

    public int getTRIGGER_PIN() {
        return TRIGGER_PIN;
    }

    public int getECHO_PIN() {
        return ECHO_PIN;
    }
}
