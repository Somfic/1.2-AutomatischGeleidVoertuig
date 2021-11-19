package Logic;

import Hardware.Buzzer;
import TI.BoeBot;
import TI.PinMode;
import TI.Timer;

public class BuzzerLogic {
    private int pin;

    private boolean state;
    private Timer timer;

    private Buzzer buzzer;

    public BuzzerLogic(Buzzer buzzer) {
        this.buzzer = buzzer;
        timer = new Timer(0);
    }

    public void setInterval(int interval) {
        this.timer.setInterval(interval);
    }

    public void setFrequency(int frequency) {
        this.buzzer.setFrequency(frequency);
    }

    public void buzz(int beepLength) {
        if(timer.timeout()) {
            timer.mark();

            if(this.state) {
                this.buzzer.buzz(beepLength);
                this.state = false;
                System.out.println("Uit");
            } else {
                this.state = true;
                System.out.println("Aan");
            }
        }
    }
}
