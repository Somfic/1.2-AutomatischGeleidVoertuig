package Hardware;

import TI.BoeBot;
import TI.PinMode;
import TI.Timer;
import java.awt.*;
import java.sql.Time;

public class Buzzer {
    private int pin;
    private int frequency;
    private boolean state;
    private Timer timer;

    public Buzzer(int pin){
    this.pin = pin;
    BoeBot.setMode(pin, PinMode.Output);
    this.timer = new Timer(0);

    }

    public void setFrequency(int frequency){
        this.frequency = frequency;
        this.timer.setInterval(frequency);
    }
    public void buzz (){

            if(timer.timeout()) {
                timer.mark();

                if(this.state) {
                    BoeBot.freqOut(this.pin,30000,1000);
                    this.state = false;
                    System.out.println("Uit");
                } else {
                    this.state = true;
                    System.out.println("Aan");
                }
            }
        }

    }

