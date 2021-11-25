package Logic;

import Hardware.Buzzer;
import TI.Timer;

/**
 * The BuzzerLogic class is used to control a buzzer.
 */
public class BuzzerLogic implements Logic {
    private boolean state;
    private Timer timer;
    private boolean active = false;

    private Buzzer buzzer = new Buzzer(5);

    /**
     * Constructor for the BuzzerLogic class.
     */
    public BuzzerLogic() {
        timer = new Timer(300);
    }

    public void setIsBuzzing(boolean state) {
        this.active = state;
    }

    /**
     * Sets the interval of the buzzer.
     * @param interval The interval of the buzzer in milliseconds.
     */
    public void setInterval(int interval) {
        this.timer.setInterval(interval);
    }

    /**
     * Sets the tone frequency of the buzzer.
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
                this.buzzer.play(10);
            }
        }
    }

    /**
     * Resets the buzzer.
     */
    @Override
    public void reset(){
        this.buzzer.play(0);
    }
}
