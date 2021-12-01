package System;


/**
 * A system combines multiple logics to produce a behavior.
 */
public interface SystemInterface {

    void initialise();

    void process();

    void reset();
}
