import Hardware.Leds;
import TI.BoeBot;
import TI.PinMode;

import java.awt.*;

public class RobotMain {

    public static void main(String[] args) {

        Leds leds = new Leds(false);
        leds.setFrequency(600);

        while(true) {
            BoeBot.wait(1);
            leds.blink();
        }
    }
}
