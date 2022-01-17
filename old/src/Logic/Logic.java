package Logic;

/**
 * Interface for the logic of the robot.
 */
public interface Logic {

    /*
     * This method is called every tick of the robot's lifecycle.
     */
    void process();

    /*
     * This method is called when the hardware needs to be reset.
     */
    void reset();
}
