package Logic;

import Hardware.Buzzer;
import TI.Timer;

/**
 * The BuzzerLogic class is used to control a BUZZER.
 */
public class BuzzerLogic implements Logic {
    private final Timer TIMER;
    private final Buzzer BUZZER;
    private boolean state;
    private boolean active = false;

    private Timer buzzTimeOut;
    private boolean buzzOnceBoolean = false;

    /**
     * Constructor for the BuzzerLogic class.
     */
    public BuzzerLogic(int pin) {
        TIMER = new Timer(300);
        this.BUZZER = new Buzzer(pin);
    }

    public void setIsBuzzing(boolean state) {
        this.active = state;
    }

    /**
     * Sets the interval of the BUZZER.
     *
     * @param interval The interval of the BUZZER in milliseconds.
     */
    public void setInterval(int interval) {
        this.TIMER.setInterval(interval);
    }

    /**
     * Sets the tone frequency of the BUZZER.
     *
     * @param frequency The frequency of the BUZZER in Hertz.
     */
    public void setFrequency(int frequency) {
        this.BUZZER.setFrequency(frequency);
    }

    /**
     * Makes the BUZZER buzz once.
     *
     * @param  length The length of the buzz in milliseconds.
     */
    public void buzzOnce(int length){
        buzzTimeOut = new Timer(length);
        this.BUZZER.play();
        this.buzzOnceBoolean = true;
    }

    /**
     * Processes the BUZZER logic.
     */
    @Override
    public void process() {
        if (active) {
            if (TIMER.timeout()) {
                TIMER.mark();

                // Toggle the BUZZER state
                this.state = !this.state;
            }

            if (this.state) {
                this.BUZZER.play();
            } else {
                this.BUZZER.stop();
            }
        } else {
            this.BUZZER.stop();
        }

        if (this.buzzOnceBoolean) {
            this.BUZZER.play();
            if (this.buzzTimeOut.timeout()) {
                this.BUZZER.stop();
                this.buzzOnceBoolean = false;
            }
        }
    }

    /**
     * Resets the BUZZER.
     */
    @Override
    public void reset() {
        this.BUZZER.stop();
    }
}
