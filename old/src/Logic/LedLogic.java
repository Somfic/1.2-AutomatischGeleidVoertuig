package Logic;

import Logger.Logger;
import TI.BoeBot;

import java.awt.*;
import java.util.HashMap;

/**
 * The LedLogic class is used to control the blinking of the LEDs.
 */
public class LedLogic implements Logic {

    private final Logger LOGGER = new Logger(this);

    private HashMap<LedLight, Color> pendingColors;
    private HashMap<LedLight, Color> setColors;

    public LedLogic() {
        this.pendingColors = new HashMap<>();
        this.setColors = new HashMap<>();
    }

    public void set(LedLight light, Color color) {
        this.pendingColors.put(light, color);
    }

    public void clear(LedLight light) {
        set(light, Color.black);
    }

    @Override
    public void process() {
        for (LedLight light : LedLight.values()) {

            Color color = pendingColors.getOrDefault(light, Color.black);

            // Set the color of the LED if it is not already set or if the color is different
            if (!this.setColors.containsKey(light) || !this.setColors.get(light).equals(color)) {

                this.setColors.put(light, color);

                float brightness = 0.1f;

                int r = (int) Math.ceil(color.getRed() * brightness);
                int g = (int) Math.ceil(color.getGreen() * brightness);
                int b = (int) Math.ceil(color.getBlue() * brightness);

                //LOGGER.debug("Setting LED " + light.toString() + " to " + " (" + r + ", " + g + ", " + b + ")");

                BoeBot.rgbSet(getLightAddress(light), r, g, b);
                BoeBot.rgbShow();
            }
        }

        BoeBot.rgbShow();
    }

    @Override
    public void reset() {
        this.setColors = new HashMap<>();
        this.pendingColors = new HashMap<>();

        for (int i = 0; i <= 6; i++) {
            BoeBot.rgbSet(i, Color.black);
        }

        BoeBot.rgbShow();
    }

    public int getLightAddress(LedLight led) {
        switch (led) {
            case BACK_RIGHT:
                return 0;

            case BACK_MIDDLE:
                return 1;

            case BACK_LEFT:
                return 2;

            case FRONT_LEFT:
                return 3;

            case FRONT_MIDDLE:
                return 4;

            case FRONT_RIGHT:
                return 5;

            default:
                return -1;
        }
    }
}


