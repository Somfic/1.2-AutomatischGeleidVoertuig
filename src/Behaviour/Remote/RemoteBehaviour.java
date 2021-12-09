package Behaviour.Remote;

import Behaviour.Behaviour;
import Logger.Logger;
import Logic.InfraredLogic;

public class RemoteBehaviour implements Behaviour {

    private Logger logger = new Logger(this);

    private RemoteListener remoteListener;
    private InfraredLogic infraredLogic;

    public RemoteBehaviour(RemoteListener remoteListener, InfraredLogic infraredLogic) {

        this.remoteListener = remoteListener;
        this.infraredLogic = infraredLogic;
    }

    @Override
    public void initialise() {

    }

    private int lastCode = 0;

    @Override
    public void process() {
        int newCode = this.infraredLogic.getLastCode();

        if(this.lastCode != newCode) {
            this.lastCode = newCode;

            if(newCode == -1) {
                this.logger.debug("Released button");
            } else {
                this.logger.debug("Pressed " + newCode);
            }

            this.remoteListener.onRemoteButtonPressed(newCode);
        }
    }

    @Override
    public void reset() {

    }
}
