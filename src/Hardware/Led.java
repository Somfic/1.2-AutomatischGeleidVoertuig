package Hardware;


import TI.BoeBot;

import java.awt.*;

/**
 * The Led class is used to control a LED.
 */
public class Led {
    private final int pin;

    /**
     * Constructor for the LED class.
     *
     * @param pin The pin the LED is connected to.
     */
    public Led(int pin) {
        this.pin = pin;
    }

    /**
     * Sets the color of the LED.
     *
     * @param color The color of the LED.
     */
    public void set(Color color) {
        BoeBot.rgbSet(pin, color);
        BoeBot.rgbShow();
    }

    /**
     * Clears the LED.
     */
    public void reset() {
        BoeBot.rgbSet(pin, Color.black);
        BoeBot.rgbShow();
    }
}
