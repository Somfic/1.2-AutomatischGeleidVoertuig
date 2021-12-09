package Behaviour.StartStop;

import Behaviour.Behaviour;
import Configuration.Config;
import Hardware.Switch;
import Logger.Logger;
import Logic.BuzzerLogic;
import Logic.InfraredLogic;
import TI.Timer;

public class StartStopBehaviour implements Behaviour {

    private Logger logger = new Logger(this);

    private Switch startButton;
    private Switch stopButton;

    private InfraredLogic infrared;

    private StartStopListener startStopListener;

    private boolean shouldStop;


    public StartStopBehaviour(StartStopListener startStopListener, InfraredLogic infrared) {
        this.startStopListener = startStopListener;
        this.infrared = infrared;
    }

    @Override
    public void initialise() {
        this.startButton = new Switch(Config.BUTTON_EMERGENCY_STOP_PIN);
        this.stopButton = new Switch(Config.BUTTON_START_PIN);
    }

    @Override
    public void process() {

        // Process the infrared
        infrared.process();

        // Check if the start button is pressed
        if (infrared.getLastCode() == Config.REMOTE_POWER || this.stopButton.getState()) {

            // Only trigger the stop behaviour is the robot is currently running
            if (!this.shouldStop) {

                this.shouldStop = true;

                this.logger.info("Stopping the robot");

                // Invoke the callback, the robot should stop
                this.startStopListener.onStartStop(true);
            }
        }

        // Check if the stop button is pressed
        if (infrared.getLastCode() == Config.REMOTE_MUTE || this.startButton.getState()) {

            // Only trigger the start behaviour is the robot is currently stopped
            if (this.shouldStop) {

                this.shouldStop = false;

                this.logger.info("Starting the robot");

                // Invoke the callback, the robot should start
                this.startStopListener.onStartStop(false);
            }
        }
    }

    public boolean shouldStop() {
        return this.shouldStop;
    }

    @Override
    public void reset() {
        this.infrared.reset();
    }
}
