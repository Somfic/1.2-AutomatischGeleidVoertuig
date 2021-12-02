package Behaviour;


import Logic.BuzzerLogic;
import Logic.InfraredLogic;
import Logic.MotorLogic;

public class BuzzerBehaviour implements Behaviour {

    private final BuzzerLogic buzzerLogic;
    private final InfraredLogic infraredLogic;
    private MotorLogic motorLogic;

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
