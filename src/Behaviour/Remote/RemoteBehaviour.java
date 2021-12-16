package Behaviour.Remote;

import Behaviour.Behaviour;
import Logger.Logger;
import Logic.InfraredLogic;

public class RemoteBehaviour implements Behaviour {

    private final Logger LOGGER = new Logger(this);

    private final RemoteListener REMOTE_LISTENER;
    private final InfraredLogic INFRARED_LOGIC;
    private int lastCode = 0;

    public RemoteBehaviour(RemoteListener remoteListener, InfraredLogic infraredLogic) {

        this.REMOTE_LISTENER = remoteListener;
        this.INFRARED_LOGIC = infraredLogic;
    }

    @Override
    public void initialise() {

    }

    @Override
    public void process() {
        int newCode = this.INFRARED_LOGIC.getLastCode();

        if (this.lastCode != newCode) {
            this.lastCode = newCode;

            if (newCode == -1) {
                this.LOGGER.debug("Released button");
            } else {
                this.LOGGER.debug("Pressed " + newCode);
            }

            this.REMOTE_LISTENER.onRemoteButtonPressed(newCode);
        }
    }

    @Override
    public void reset() {

    }
}
