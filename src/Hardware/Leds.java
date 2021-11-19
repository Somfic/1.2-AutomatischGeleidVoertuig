package Hardware;

import TI.BoeBot;
import TI.Timer;

import java.awt.*;

public class Leds {
    private int pin1;
    private int pin2;

    private Timer timer;

    private boolean state = true;

    public Leds(boolean isLeft) {
        if(isLeft) {
            this.pin1 = 2;
            this.pin2 = 3;
        } else {
            this.pin1 = 0;
            this.pin2 = 5;
        }

        this.timer = new Timer(0);

        for (int i = 0; i < 6; i++) {
            BoeBot.rgbSet(i, Color.black);
        }
        BoeBot.rgbShow();
    }

    public void setFrequency(int frequency) {
        this.timer.setInterval(frequency);
    }

    public void blink() {
        Color color = new Color(194, 30, 0);

        if(timer.timeout()) {
            timer.mark();

            if(this.state) {
                BoeBot.rgbSet(this.pin1, Color.black);
                BoeBot.rgbSet(this.pin2, Color.black);
                BoeBot.rgbShow();
                this.state = false;
                System.out.println("Uit");
            } else {
                BoeBot.rgbSet(this.pin1, color);
                BoeBot.rgbSet(this.pin2, color);
                BoeBot.rgbShow();
                this.state = true;
                System.out.println("Aan");
            }
        }
     }
}
