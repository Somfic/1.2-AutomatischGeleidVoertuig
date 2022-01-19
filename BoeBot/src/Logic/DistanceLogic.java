package Logic;

import Hardware.Ultrasonic;
import TI.BoeBot;
import TI.Timer;

public class DistanceLogic implements Logic {

    private final Ultrasonic ULTRASONIC;

    private final Timer TIMER = new Timer(0);
    private float distance = 0;
    private float pulse = 69420;
    private int offset;


    public DistanceLogic(int triggerPin, int echoPin, int offset) {
        this.ULTRASONIC = new Ultrasonic(triggerPin, echoPin);
        this.offset = offset;
    }

    public float getDistance() {
        if (this.pulse != -2){
            this.distance = this.pulse/58;
        }
        return this.distance - offset;
    }

    public float getPulse() {
        return this.pulse;
    }

    @Override
    public void process() {

        if (TIMER.timeout()) {
            BoeBot.digitalWrite(this.ULTRASONIC.getTRIGGER_PIN(), true);
            BoeBot.wait(2);
            BoeBot.digitalWrite(this.ULTRASONIC.getTRIGGER_PIN(), false);
            this.pulse = BoeBot.pulseIn(this.ULTRASONIC.getECHO_PIN(), true, 10000);
            TIMER.setInterval(50);
        }
    }

    @Override
    public void reset() {
        this.TIMER.setInterval(0);
        this.TIMER.mark();
    }
}
