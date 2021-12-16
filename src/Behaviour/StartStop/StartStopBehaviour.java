package Behaviour.StartStop;

import Behaviour.Behaviour;
import Configuration.Config;
import Hardware.Switch;
import Logger.Logger;
import Logic.InfraredLogic;

public class StartStopBehaviour implements Behaviour {

    private final Logger LOGGER = new Logger(this);
    private final InfraredLogic INFRARED;
    private final StartStopListener START_STOP_LISTENER;
    private Switch startButton;
    private Switch stopButton;
    private boolean shouldStop;


    public StartStopBehaviour(StartStopListener startStopListener, InfraredLogic infrared) {
        this.START_STOP_LISTENER = startStopListener;
        this.INFRARED = infrared;
    }

    @Override
    public void initialise() {
        this.startButton = new Switch(Config.BUTTON_EMERGENCY_STOP_PIN);
        this.stopButton = new Switch(Config.BUTTON_START_PIN);
    }

    @Override
    public void process() {

        // Process the INFRARED
        INFRARED.process();

        // Check if the stop button is pressed when the start button isn't
        if (INFRARED.getLastCode() == Config.REMOTE_POWER || (this.stopButton.getState() && !this.startButton.getState())) {

            // Only trigger the stop behaviour is the robot is currently running
            if (!this.shouldStop) {

                this.shouldStop = true;

                this.LOGGER.info("Stopping the robot");

                // Invoke the callback, the robot should stop
                this.START_STOP_LISTENER.onStartStop(true);
            }
        }

        // Check if the start button is pressed when the stop button isn't
        if (INFRARED.getLastCode() == Config.REMOTE_MUTE || (this.startButton.getState() && !this.stopButton.getState())) {

            // Only trigger the start behaviour is the robot is currently stopped
            if (this.shouldStop) {

                this.shouldStop = false;

                this.LOGGER.info("Starting the robot");

                // Invoke the callback, the robot should start
                this.START_STOP_LISTENER.onStartStop(false);
            }
        }
    }

    public boolean shouldStop() {
        return this.shouldStop;
    }

    @Override
    public void reset() {
        this.INFRARED.reset();
    }
}
