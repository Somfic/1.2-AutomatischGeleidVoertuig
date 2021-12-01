package System;

import Hardware.Switch;

public class EmergencySystem implements SystemInterface {

    private Switch startButton;
    private Switch stopButton;

    private boolean isRunning;

    @Override
    public void initialise() {
        this.startButton = new Switch(0);
        this.stopButton = new Switch(1);
    }

    @Override
    public void process() {
        if(this.startButton.getState()) {
            this.isRunning = true;
        }

        if(this.stopButton.getState()) {
            this.isRunning = false;
        }
    }

    public boolean shouldStop() {
        return !this.isRunning;
    }

    @Override
    public void reset() {

    }
}
