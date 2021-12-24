package Hardware;


import TI.BoeBot;

import java.awt.*;

/**
 * The Led class is used to control a LED.
 */
public class Led {
    private final int PIN;

    /**
     * Constructor for the LED class.
     *
     * @param pin The PIN the LED is connected to.
     */
    public Led(int pin) {
        this.PIN = pin;
    }

    /**
     * Sets the color of the LED.
     *
     * @param color The color of the LED.
     */
    public void set(Color color) {
        BoeBot.rgbSet(PIN, color);
        BoeBot.rgbShow();
    }

    /**
     * Clears the LED.
     */
    public void reset() {
        BoeBot.rgbSet(PIN, Color.black);
        BoeBot.rgbShow();
    }
}
