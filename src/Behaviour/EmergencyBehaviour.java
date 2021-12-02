package Behaviour;

import Configuration.Config;
import Hardware.Switch;
import Logic.InfraredLogic;

public class EmergencyBehaviour implements Behaviour {

    private Switch startButton;
    private Switch stopButton;

    private boolean isRunning;
    private InfraredLogic infrared;


    public EmergencyBehaviour(InfraredLogic infrared) {

        this.infrared = infrared;
    }

    @Override
    public void initialise() {
        this.startButton = new Switch(Config.BUTTON_EMERGENCY_STOP_PIN);
        this.stopButton = new Switch(Config.BUTTON_START_PIN);
    }

    @Override
    public void process() {
        infrared.process();

        if(infrared.getLastCode() == Config.REMOTE_POWER) {
            this.isRunning = false;
        }

        if(infrared.getLastCode() == Config.REMOTE_MUTE) {
            this.isRunning = true;
        }

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
