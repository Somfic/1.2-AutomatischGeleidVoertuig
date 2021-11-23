package Logic;

import Hardware.*;
import TI.*;

import java.awt.*;

/**
 * The BlinkerLogic class is used to control the blinking of the LEDs.
 */
public class BlinkerLogic implements Logic {
    private final Color blinkColor = new Color(194, 30, 0);

    private boolean state = true;

    private boolean isBlinkingLeft;
    private boolean isBlinkingRight;

    private Timer timer;

    private Led frontLeft;
    private Led frontRight;
    private Led backLeft;
    private Led backRight;

    /**
     * Constructor for the BlinkerLogic class.
     *
     * @param frontLeft  The front left LED.
     * @param frontRight The front right LED.
     * @param backLeft   The back left LED.
     * @param backRight  The back right LED.
     */
    public BlinkerLogic(Led frontLeft, Led frontRight, Led backLeft, Led backRight) {
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;

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
                        this.frontLeft.set(this.blinkColor);
                        this.backLeft.set(this.blinkColor);
                    }

                    if (isBlinkingRight) {
                        this.frontRight.set(this.blinkColor);
                        this.backRight.set(this.blinkColor);
                    }
                }
            }
        }
    }
}
