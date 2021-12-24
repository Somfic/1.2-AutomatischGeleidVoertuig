package Logic;

import Hardware.Ultrasonic;
import TI.BoeBot;
import TI.Timer;

public class DistanceLogic implements Logic {

    private final Ultrasonic ULTRASONIC;

    private final Timer TIMER = new Timer(0);
    private float distance = 0;
    private Step step = Step.RESET_PULSE;
    private float pulse;


    public DistanceLogic(int triggerPin, int echoPin) {
        this.ULTRASONIC = new Ultrasonic(triggerPin, echoPin);
        this.pulse = 0;
    }

    public float getDistance() {
        if (this.pulse != -2){
            this.distance = this.pulse/58;
        }
        return this.distance;
    }

    public float getPulse() {
        return this.pulse;
    }

    @Override
    public void process() {

        if (TIMER.timeout()) {
            BoeBot.digitalWrite(10, true);
            BoeBot.wait(1);
            BoeBot.digitalWrite(10, false);
            this.pulse = BoeBot.pulseIn(11, true, 10000);
            TIMER.setInterval(50);
        }
    }

    @Override
    public void reset() {
        this.step = Step.READ_ECHO;
        this.TIMER.setInterval(0);
        this.TIMER.mark();
    }


    private enum Step {
        RESET_PULSE,
        START_PULSE,
        END_PULSE,
        READ_ECHO
    }
}
