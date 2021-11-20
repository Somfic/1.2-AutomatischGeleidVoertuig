package Hardware;

import TI.*;

/**
 * The Buzzer class is used to control a buzzer.
 */
public class Buzzer {
    private int pin;
    private int frequency;

    /**
     * Constructor for the Buzzer class.
     * @param pin The pin the buzzer is connected to.
     */
    public Buzzer(int pin) {
        BoeBot.setMode(pin, PinMode.Output);
        this.pin = pin;
    }

    /**
     * Sets the frequency of the tone.
     * @param frequency The frequency of the tone.
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * Plays a tone.
     * @param time The duration of the tone.
     */
    public void play(int time) {
        BoeBot.freqOut(this.pin, this.frequency, time);
    }
}

