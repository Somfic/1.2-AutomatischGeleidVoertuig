package Logic;

import Hardware.Ultrasonic;
import TI.BoeBot;
import TI.Timer;

public class DistanceLogic implements Logic {

    private Ultrasonic ultrasonic;

    private Timer timer = new Timer(0);
    private Step step = Step.ResetPulse;

    private float distance = 0;
    private float pulse = 0;

    public DistanceLogic(int triggerPin, int echoPin) {
        this.ultrasonic = new Ultrasonic(triggerPin, echoPin);
    }

    public float getDistance() {
        return this.distance;
    }

    public float getPulse() {
        return this.pulse;
    }

    @Override
    public void process() {
//        if(timer.timeout()) {
//            if (this.step == Step.ResetPulse) {
//                ultrasonic.set(false);
//
//                this.timer.setInterval(2);
//                this.timer.mark();
//
//                this.step = Step.StartPulse;
//            } else if (this.step == Step.StartPulse) {
//                ultrasonic.set(true);
//
//                this.timer.setInterval(10);
//                this.timer.mark();
//
//                this.step = Step.EndPulse;
//            } else if (this.step == Step.EndPulse) {
//                ultrasonic.set(false);
//
//                this.timer.setInterval(1);
//                this.timer.mark();
//
//                this.step = Step.ReadEcho;
//            } else if (this.step == Step.ReadEcho) {
//                int pulse = ultrasonic.read();
//
//                this.timer.setInterval(1000);
//                this.timer.mark();
//
//                this.step = Step.ResetPulse;
//
//                this.pulse = pulse;
//                this.distance = pulse * 0.034f / 2f;
//            }
//        }

        ultrasonic.set(false);

        BoeBot.wait(5);

        ultrasonic.set(true);

        BoeBot.wait(10);

        ultrasonic.set(false);

        while(!ultrasonic.read()) {

        }

        long startTime = System.nanoTime();

        while(ultrasonic.read()) {

        }

        long endTime = System.nanoTime();

        this.pulse = (endTime - startTime);
        this.distance = (float) (pulse / 1e3 /2 /29.1);
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
