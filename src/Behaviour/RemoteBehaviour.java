package Behaviour;

import Logic.InfraredLogic;

public class RemoteBehaviour implements Behaviour {

    private InfraredLogic infraredLogic;

    public RemoteBehaviour(InfraredLogic infraredLogic) {

        this.infraredLogic = infraredLogic;
    }

    @Override
    public void initialise() {

    }

    private int lastCode = 0;

    @Override
    public void process() {
        this.infraredLogic.process();

        int newCode = this.infraredLogic.getLastCode();
        if(this.lastCode != newCode) {
            this.lastCode = newCode;
        }
    }

    @Override
    public void reset() {

    }
}
