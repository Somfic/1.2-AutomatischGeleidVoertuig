package Hardware;

import TI.BoeBot;
import TI.PinMode;

public class Ultrasonic {

    private final int triggerPin;
    private final int echoPin;

    public Ultrasonic(int triggerPin, int echoPin) {
        this.triggerPin = triggerPin;
        this.echoPin = echoPin;

        BoeBot.setMode(this.triggerPin, PinMode.Output);
        BoeBot.setMode(this.echoPin, PinMode.Input);
    }

    public void set(boolean value) {
        BoeBot.setMode(this.triggerPin, PinMode.Output);
        BoeBot.digitalWrite(this.triggerPin, value);
    }

    //
    public boolean read() {
        BoeBot.setMode(this.echoPin, PinMode.Input);
        return BoeBot.digitalRead(this.echoPin);
    }


//    public int read() {
//        BoeBot.setMode(this.echoPin, PinMode.Input);
//        return BoeBot.pulseIn(this.echoPin, true, 10000);
//    }
}
