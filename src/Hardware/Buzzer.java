package Hardware;

import TI.*;

/**
 * The Buzzer class is used to control a buzzer.
 */
public class Buzzer {
    private int pin;

    private PWM pwm;

    /**
     * Constructor for the Buzzer class.
     * @param pin The pin the buzzer is connected to.
     */
    public Buzzer(int pin) {
        BoeBot.setMode(pin, PinMode.Output);
        this.pwm = new PWM(pin, 254);
        this.pin = pin;
    }

    /**
     * Sets the frequency of the tone.
     * @param frequency The frequency of the tone.
     */
    public void setFrequency(int frequency) {
        this.pwm.update(frequency);
    }

    /**
     * Starts playing a tone.
     */
    public void play() {
        this.pwm.start();
    }

    /**
     * Stops playing a tone.
     */
    public void stop() {
        this.pwm.stop();
    }
}

