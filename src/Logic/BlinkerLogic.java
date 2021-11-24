package Logic;

import Hardware.Led;
import TI.*;

import java.awt.*;

/**
 * The BlinkerLogic class is used to control the blinking of the LEDs.
 */
public class BlinkerLogic implements Logic {
    private final Color BLINKCOLOR = new Color(194, 30, 0);

    private boolean state = true;

    private boolean isBlinkingLeft;
    private boolean isBlinkingRight;

    private Timer timer;

    private Led frontLeft = new Led(3);
    private Led frontRight = new Led(5);
    private Led backLeft = new Led(2);
    private Led backRight = new Led(0);

    private Led[] leds = {frontLeft, frontRight, backLeft, backRight};

    /**
     * Constructor for the BlinkerLogic class.
     */
    public BlinkerLogic() {
        this.timer = new Timer(300);
    }

    /**
     * Sets the interval of the blinker.
     *
     * @param interval The interval of the blinker in milliseconds.
     */
    public void setBlinkInterval(int interval) {
        this.timer.setInterval(interval);
    }

    /**
     * Sets whether the blinker is blinking on the left.
     *
     * @param isBlinking The state of the blinker.
     */
    public void setBlinkLeft(boolean isBlinking) {
        this.isBlinkingLeft = isBlinking;
    }

    /**
     * Sets whether the blinker is blinking on the right.
     *
     * @param isBlinking The state of the blinker.
     */
    public void setBlinkRight(boolean isBlinking) {
        this.isBlinkingRight = isBlinking;
    }

    /**
     * Processes the blinker logic.
     */
    @Override
    public void process() {
        if (!this.isBlinkingLeft && !this.isBlinkingRight) {
            // No blinking needed, reset
            this.timer.mark();
            this.state = false;

            this.frontLeft.reset();
            this.frontRight.reset();
            this.backLeft.reset();
            this.backRight.reset();
        } else {
            if (this.timer.timeout()) {
                // Reset the timer
                this.timer.mark();

                // Toggle the state
                this.state = !this.state;

                // Reset all the LEDs
                this.frontLeft.reset();
                this.frontRight.reset();
                this.backLeft.reset();
                this.backRight.reset();

                if (this.state) {
                    if (isBlinkingLeft) {
                        this.frontLeft.set(this.BLINKCOLOR);
                        this.backLeft.set(this.BLINKCOLOR);
                    }

                    if (isBlinkingRight) {
                        this.frontRight.set(this.BLINKCOLOR);
                        this.backRight.set(this.BLINKCOLOR);
                    }
                }
            }
        }
    }

    /**
     * Resets all the leds it owns.
     */
    @Override
    public void reset(){
        for (Led led : this.leds) {
            led.reset();
        }
    }
}
