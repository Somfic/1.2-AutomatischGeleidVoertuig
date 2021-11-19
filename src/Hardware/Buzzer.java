package Hardware;

import TI.BoeBot;
import TI.PinMode;
import TI.Timer;
import java.awt.*;
import java.sql.Time;

public class Buzzer {
    private int pin;
    private int frequency;

    public Buzzer(int pin) {
        BoeBot.setMode(pin, PinMode.Output);
        this.pin = pin;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void buzz(int time) {
        BoeBot.freqOut(this.pin, this.frequency, time);
    }
}

