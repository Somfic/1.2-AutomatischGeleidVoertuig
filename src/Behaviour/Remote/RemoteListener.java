package Behaviour.Remote;

/**
 * This interface is used to listen to the buttons on the remote.
 */
public interface RemoteListener {

    /**
     * This method is called when a button on the remote is pressed.
     */
    void onRemoteButtonPressed(int code);
}
