package Hardware;


import TI.BoeBot;
import TI.PWM;
import TI.PinMode;

/**
 * The Buzzer class is used to control a buzzer.
 */
public class Buzzer {
    private final int PIN;

    private final PWM PWM;

    /**
     * Constructor for the Buzzer class.
     *
     * @param pin The PIN the buzzer is connected to.
     */
    public Buzzer(int pin) {
        BoeBot.setMode(pin, PinMode.Output);
        this.PWM = new PWM(pin, 254);
        this.PIN = pin;
    }

    /**
     * Sets the frequency of the tone.
     *
     * @param frequency The frequency of the tone.
     */
    public void setFrequency(int frequency) {
        this.PWM.update(frequency);
    }

    /**
     * Starts playing a tone.
     */
    public void play() {
        this.PWM.start();
    }

    /**
     * Stops playing a tone.
     */
    public void stop() {
        this.PWM.stop();
    }
}

