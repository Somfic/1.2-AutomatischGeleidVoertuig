package Logic;

import TI.*;

import java.awt.*;

/**
 * The LedLogic class is used to control the blinking of the LEDs.
 */
public class LedLogic implements Logic {
    public void set(LedLight light, Color color) {

        float brightness = 0.1f;

        int r = (int)Math.ceil(color.getRed() * brightness);
        int g = (int)Math.ceil(color.getGreen() * brightness);
        int b = (int)Math.ceil(color.getBlue() * brightness);

        BoeBot.rgbSet(getLightAddress(light), r, g, b);
    }

    public void clear(LedLight light) {
        BoeBot.rgbSet(getLightAddress(light), Color.black);
    }

    @Override
    public void process() {
        BoeBot.rgbShow();
    }

    @Override
    public void reset() {
        for (int i = 0; i <= 6; i++) {
            BoeBot.rgbSet(i, Color.black);
        }
        BoeBot.rgbShow();
    }

    public int getLightAddress(LedLight led) {
        switch (led) {
            case BackRight:
                return 0;

            case BackMiddle:
                return 1;

            case BackLeft:
                return 2;

            case FrontLeft:
                return 3;

            case FrontMiddle:
                return 4;

            case FrontRight:
                return 5;

            default:
                return -1;
        }
    }
}


