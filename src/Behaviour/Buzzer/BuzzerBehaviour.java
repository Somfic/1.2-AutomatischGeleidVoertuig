package Behaviour.Buzzer;

import Behaviour.Behaviour;
import Logger.Logger;
import Logic.BuzzerLogic;
import Logic.InfraredLogic;
import Logic.MotorLogic;

public class BuzzerBehaviour implements Behaviour {

    private final Logger logger = new Logger(this);

    private final BuzzerLogic buzzerLogic;
    private final InfraredLogic infraredLogic;
    private final MotorLogic motorLogic;

    public BuzzerBehaviour(BuzzerLogic buzzerLogic, InfraredLogic infraredLogic, MotorLogic motorLogic) {
        this.buzzerLogic = buzzerLogic;
        this.infraredLogic = infraredLogic;
        this.motorLogic = motorLogic;
    }

    @Override
    public void initialise() {

    }

    @Override
    public void process() {
        boolean shouldBeep = this.motorLogic.getTargetSpeed() < 0;

        this.buzzerLogic.setIsBuzzing(shouldBeep);
    }

    @Override
    public void reset() {
    }
}
