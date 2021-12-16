package Logic;

import Hardware.Infrared;
import TI.Timer;

public class InfraredLogic implements Logic {

    private final Infrared INFRARED;
    private final Timer TIMEOUT = new Timer(250);
    private int lastCode;

    public InfraredLogic(int pin) {
        this.INFRARED = new Infrared(pin);
    }

    @Override
    public void process() {
        int pulseLength = INFRARED.getValue(false, 10000);

        if (pulseLength > 2000) {
            TIMEOUT.mark();

            // Started a new signal

            int[] lengths = new int[12];
            int output = 0;

            for (int i = 0; i < 12; i++) {
                lengths[i] = INFRARED.getValue(false, 10000);

                if (lengths[i] > 800) {
                    output = output | (1 << i);
                }

                // -2 indicates an error code while trying to read out the pulse
                if (lengths[i] == -2) {
                    return;
                }
            }

            this.lastCode = output;
        } else {
            if (TIMEOUT.timeout()) {
                this.lastCode = -1;
            }
        }
    }

    public int getLastCode() {
        return this.lastCode;
    }

    @Override
    public void reset() {
        this.lastCode = -1;
    }
}
