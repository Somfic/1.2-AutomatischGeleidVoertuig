package Behaviour;


/**
 * A system combines multiple logics to produce a behavior.
 */
public interface Behaviour {

    void initialise();

    void process();

    void reset();
}
