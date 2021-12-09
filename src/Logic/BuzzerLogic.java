package Logic;

import Hardware.Buzzer;
import TI.Timer;

/**
 * The BuzzerLogic class is used to control a buzzer.
 */
public class BuzzerLogic implements Logic {
    private final Timer timer;
    private final Buzzer buzzer;
    private boolean state;
    private boolean active = false;

    /**
     * Constructor for the BuzzerLogic class.
     */
    public BuzzerLogic(int pin) {
        timer = new Timer(300);
        this.buzzer = new Buzzer(pin);
    }

    public void setIsBuzzing(boolean state) {
        this.active = state;
    }

    /**
     * Sets the interval of the buzzer.
     *
     * @param interval The interval of the buzzer in milliseconds.
     */
    public void setInterval(int interval) {
        this.timer.setInterval(interval);
    }

    /**
     * Sets the tone frequency of the buzzer.
     *
     * @param frequency The frequency of the buzzer in Hertz.
     */
    public void setFrequency(int frequency) {
        this.buzzer.setFrequency(frequency);
    }

    /**
     * Processes the buzzer logic.
     */
    @Override
    public void process() {
        if (active) {
            if (timer.timeout()) {
                timer.mark();

                // Toggle the buzzer state
                this.state = !this.state;
            }

            if (this.state) {
                this.buzzer.play();
            } else {
                this.buzzer.stop();
            }
        } else {
            this.buzzer.stop();
        }
    }

    /**
     * Resets the buzzer.
     */
    @Override
    public void reset() {
        this.buzzer.stop();
    }
}
