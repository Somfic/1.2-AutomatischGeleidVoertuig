package Behaviour;

import Hardware.Switch;
import Logic.MotorLogic;
import TI.Timer;

import java.util.ArrayList;

public class MovementBehaviour implements Behaviour {

    private MotorLogic motor;

    private Switch leftWhisker;
    private Switch rightWhisker;

    private Timer timer;

    private ArrayList<Movement> movementQueue = new ArrayList<Movement>();
    private boolean isExecutingMovement;

    public MovementBehaviour(MotorLogic motorLogic) {
        this.motor = motorLogic;
    }

    @Override
    public void initialise() {
        this.leftWhisker = new Switch(11);
        this.rightWhisker = new Switch(14);

        this.timer = new Timer(100);
    }

    @Override
    public void process() {
        if(timer.timeout()) {
            timer.mark();

            // Previous command finished executing, remove it from the commands list
            if (isExecutingMovement && this.movementQueue.size() > 0) {
                this.movementQueue.remove(0);
                isExecutingMovement = false;
            }

            // If there are more commands to run, run those first
            if (this.movementQueue.size() > 0) {
                Movement movement = this.movementQueue.get(0);

                motor.setAcceleration(movement.acceleration);
                motor.setMove(movement.speed, movement.angle);

                timer.setInterval(movement.duration);
                timer.mark();

                isExecutingMovement = true;
                System.out.println("[START] " + movement.name + " (" + movement.duration + "ms)");

                return;
            }

            // All queued commands are executed, find a new command
            timer.setInterval(100);

            if (this.leftWhisker.getState() && this.rightWhisker.getState()) {
                addMovementToQueue("Braking", 0, 0, 10, 500);
                addMovementToQueue("Backing up", -0.5f, 0, 5, 1500);
                addMovementToQueue("Stopping", 0, 0, 5,500);
                return;
            }

            if (this.rightWhisker.getState()) {
                addMovementToQueue("Braking", 0, 0, 10,500);
                addMovementToQueue("Backing up for left turn", -0.5f, 0, 5,3000);
                addMovementToQueue("Turn left", 0, 0.5f, 10,1700);
                addMovementToQueue("Stopping", 0, 0, 5,500);

                return;

            }

            if (this.leftWhisker.getState()) {
                addMovementToQueue("Braking", 0, 0, 10,500);
                addMovementToQueue("Backing up for right turn", -0.5f, 0,  5,3000);
                addMovementToQueue("Turn right", 0, -0.5f, 10,1700);
                addMovementToQueue("Stopping", 0, 0, 5,500);
                return;

            }

            motor.setAcceleration(2);
            motor.setMove(1, 0);
        }
    }

    private void addMovementToQueue(String name, float targetSpeed, float targetAngle, int acceleration, int duration) {
        Movement movement = new Movement(name, targetSpeed, targetAngle, acceleration, duration);
        movementQueue.add(movement);
    }

    @Override
    public void reset() {
        movementQueue.clear();
        isExecutingMovement = false;
    }
}


