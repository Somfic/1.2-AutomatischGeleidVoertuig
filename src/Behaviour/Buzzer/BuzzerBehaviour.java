package Behaviour.Buzzer;

import Behaviour.Behaviour;
import Logger.Logger;
import Logic.BuzzerLogic;
import Logic.MotorLogic;

public class BuzzerBehaviour implements Behaviour {

    private final Logger LOGGER = new Logger(this);

    private final BuzzerLogic BUZZER_LOGIC;
    private final MotorLogic MOTOR_LOGIC;

    public BuzzerBehaviour(BuzzerLogic buzzerLogic, MotorLogic motorLogic) {
        this.BUZZER_LOGIC = buzzerLogic;
        this.MOTOR_LOGIC = motorLogic;
    }

    @Override
    public void initialise() {

    }

    @Override
    public void process() {
        boolean shouldBeep = this.MOTOR_LOGIC.getTargetSpeed() < 0;

        this.BUZZER_LOGIC.setIsBuzzing(shouldBeep);
    }

    @Override
    public void reset() {
    }
}
