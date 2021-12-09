package Behaviour.Movement;

import Behaviour.Behaviour;
import Behaviour.Remote.RemoteListener;
import Configuration.Config;
import Hardware.Switch;
import Logger.Logger;
import Logic.InfraredLogic;
import Logic.MotorLogic;
import Logic.WhiskerLogic;
import TI.Timer;

import java.util.ArrayList;

public class MovementBehaviour implements Behaviour, RemoteListener {

    private final Logger logger = new Logger(this);

    private MotorLogic motor;
    private WhiskerLogic whiskers;

    private Timer timer;

    private ArrayList<Movement> movementQueue = new ArrayList<Movement>();
    private boolean isExecutingMovement;

    public MovementBehaviour(MotorLogic motorLogic, WhiskerLogic whiskers) {
        this.motor = motorLogic;
        this.whiskers = whiskers;
    }

    private MoveDirection moveDirection = MoveDirection.Stationary;
    private float acceleration = 5;

    @Override
    public void initialise() {
        this.timer = new Timer(100);
    }

    @Override
    public void process() {
        if(timer.timeout()) {
            timer.mark();

            // Previous command finished executing, remove it from the commands list
            if (isExecutingMovement && this.movementQueue.size() > 0) {
                Movement movement = this.movementQueue.get(0);

                logger.debug("Finished " + movement.name);

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
                logger.debug("Starting " + movement.name + " (" + movement.duration + "ms)");

                return;
            }

            // All queued commands are executed, find a new command
            timer.setInterval(100);

            if (this.whiskers.hasObstacleLeft() && this.whiskers.hasObstacleRight()) {
                addMovementToQueue("Braking", 0, 0, 100, 500);
                addMovementToQueue("Backing up", -0.5f, 0, 5, 1500);
                addMovementToQueue("Stopping", 0, 0, 5,500);
                return;
            }

            if (this.whiskers.hasObstacleRight()) {
                addMovementToQueue("Braking", 0, 0, 100,500);
                addMovementToQueue("Backing up for left turn", -0.5f, 0, 5,3000);
                addMovementToQueue("Turn left", 0, 0.5f, 10,1700);
                addMovementToQueue("Stopping", 0, 0, 5,500);

                return;

            }

            if (this.whiskers.hasObstacleLeft()) {
                addMovementToQueue("Braking", 0, 0, 100,500);
                addMovementToQueue("Backing up for right turn", -0.5f, 0,  5,3000);
                addMovementToQueue("Turn right", 0, -0.5f, 10,1700);
                addMovementToQueue("Stopping", 0, 0, 5,500);
                return;
            }

            this.acceleration = Math.max(1, this.acceleration);
            this.acceleration = Math.min(30, this.acceleration);

            motor.setAcceleration(this.acceleration);
            if (this.moveDirection == MoveDirection.Forwards) {
                motor.setMove(1, 0);
            } else if (this.moveDirection == MoveDirection.Backwards) {
                motor.setMove(-1, 0);
            } else if (this.moveDirection == MoveDirection.Left) {
                motor.setMove(0, 0.5f);
            } else if (this.moveDirection == MoveDirection.Right) {
                motor.setMove(0, -0.5f);
            } else if (this.moveDirection == MoveDirection.Stationary) {
                motor.setMove(0, 0);
            }
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

        this.moveDirection = MoveDirection.Stationary;
        this.acceleration = 5;
    }

    @Override
    public void onRemoteButtonPressed(int code) {
        if(code == Config.REMOTE_CHANNEL_PLUS) {

            // Move forwards
            // If we are moving backwards, stop
            if(this.moveDirection == MoveDirection.Backwards) {
                this.moveDirection = MoveDirection.Stationary;
            } else {
                this.moveDirection = MoveDirection.Forwards;
            }

        } else if(code == Config.REMOTE_CHANNEL_MIN) {

            // Move backwards
            // If we are moving forwards, stop
            if(this.moveDirection == MoveDirection.Forwards) {
                this.moveDirection = MoveDirection.Stationary;
            } else {
                this.moveDirection = MoveDirection.Backwards;
            }

        } else if(code == Config.REMOTE_VOLUME_PLUS) {

            // Move to the right
            // If we are moving to the left, stop
            if(this.moveDirection == MoveDirection.Left) {
                this.moveDirection = MoveDirection.Stationary;
            } else {
                this.moveDirection = MoveDirection.Right;
            }

        } else if(code == Config.REMOTE_VOLUME_MIN) {

            // Move to the left
            // If we are moving to the right, stop
            if(this.moveDirection == MoveDirection.Right) {
                this.moveDirection = MoveDirection.Stationary;
            } else {
                this.moveDirection = MoveDirection.Left;
            }
        } else if(code == Config.REMOTE_STOP) {
            this.moveDirection = MoveDirection.Stationary;
        } else if(code == Config.REMOTE_FORWARDS) {
            this.acceleration += 1;
        } else if(code == Config.REMOTE_BACKWARDS) {
            this.acceleration -= 1;
        }
    }
}


