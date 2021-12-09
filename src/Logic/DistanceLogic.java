package Logic;

import Hardware.Ultrasonic;
import TI.BoeBot;
import TI.Timer;

public class DistanceLogic implements Logic {

    private final Ultrasonic ultrasonic;

    private final Timer timer = new Timer(0);
    private final float distance = 0;
    private Step step = Step.ResetPulse;
    private float pulse;


    public DistanceLogic(int triggerPin, int echoPin) {
        this.ultrasonic = new Ultrasonic(triggerPin, echoPin);
        this.pulse = 0;
    }

    public float getDistance() {
        return this.distance;
    }

    public float getPulse() {
        return this.pulse;
    }

    @Override
    public void process() {

        if (timer.timeout()) {
            BoeBot.digitalWrite(10, true);
            BoeBot.wait(1);
            BoeBot.digitalWrite(10, false);
            this.pulse = BoeBot.pulseIn(11, true, 10000);
            timer.setInterval(50);
        }
    }

    @Override
    public void reset() {
        this.step = Step.ReadEcho;
        this.timer.setInterval(0);
        this.timer.mark();
    }


    private enum Step {
        ResetPulse,
        StartPulse,
        EndPulse,
        ReadEcho
    }
}
