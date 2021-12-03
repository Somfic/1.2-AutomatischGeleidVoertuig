package Behaviour.StartStop;

/**
 * This interface is used to listen to the start and stop events of the
 * StartStopBehaviour.
 */
public interface StartStopListener {

    /** 
     * This method is called when the robot is stopped or started.
     */
    void onStartStop(boolean shouldStop);
}
