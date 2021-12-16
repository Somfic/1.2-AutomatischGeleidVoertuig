package Behaviour.Movement;

import Behaviour.Behaviour;
import Behaviour.Bluetooth.BluetoothListener;
import Behaviour.Remote.RemoteListener;
import Configuration.Config;
import Logger.Logger;
import Logic.DistanceLogic;
import Logic.MotorLogic;
import Logic.WhiskerLogic;
import TI.Timer;

import java.util.ArrayList;

public class MovementBehaviour implements Behaviour, RemoteListener, BluetoothListener {

    private final Logger LOGGER = new Logger(this);

    private final MotorLogic MOTOR;
    private final DistanceLogic DISTANCE;
    private final ArrayList<Movement> MOVEMENT_QUEUE = new ArrayList<Movement>();
    private WhiskerLogic whiskers;
    private Timer timer;
    private boolean isExecutingMovement;

    private MoveDirection moveDirection = MoveDirection.STATIONARY;
    private float acceleration = 5;
    public MovementBehaviour(MotorLogic motorLogic, DistanceLogic distance) {
        this.DISTANCE = distance;
        this.MOTOR = motorLogic;
    }

    @Override
    public void initialise() {
        this.timer = new Timer(100);
    }

    @Override
    public void process() {
        if (timer.timeout()) {
            timer.mark();

            // Previous command finished executing, remove it from the commands list
            if (isExecutingMovement && this.MOVEMENT_QUEUE.size() > 0) {
                Movement movement = this.MOVEMENT_QUEUE.get(0);

                LOGGER.debug("Finished " + movement.name);

                this.MOVEMENT_QUEUE.remove(0);
                isExecutingMovement = false;
            }

            // If there are more commands to run, run those first
            if (this.MOVEMENT_QUEUE.size() > 0) {
                Movement movement = this.MOVEMENT_QUEUE.get(0);

                MOTOR.setAcceleration(movement.acceleration);
                MOTOR.setMove(movement.speed, movement.angle);

                timer.setInterval(movement.duration);
                timer.mark();

                isExecutingMovement = true;
                LOGGER.debug("Starting " + movement.name + " (" + movement.duration + "ms)");

                return;
            }

            // All queued commands are executed, find a new command
            timer.setInterval(100);

            /*
            The code below is used for the whiskers
             */

//            if (this.whiskers.hasObstacleLeft() && this.whiskers.hasObstacleRight()) {
//                addMovementToQueue("Braking", 0, 0, 100, 500);
//                addMovementToQueue("Backing up", -0.5f, 0, 5, 1500);
//                addMovementToQueue("Stopping", 0, 0, 5,500);
//                return;
//            }
//
//            if (this.whiskers.hasObstacleRight()) {
//                addMovementToQueue("Braking", 0, 0, 100,500);
//                addMovementToQueue("Backing up for left turn", -0.5f, 0, 5,3000);
//                addMovementToQueue("Turn left", 0, 0.5f, 10,1700);
//                addMovementToQueue("Stopping", 0, 0, 5,500);
//
//                return;
//
//            }
//
//            if (this.whiskers.hasObstacleLeft()) {
//                addMovementToQueue("Braking", 0, 0, 100,500);
//                addMovementToQueue("Backing up for right turn", -0.5f, 0,  5,3000);
//                addMovementToQueue("Turn right", 0, -0.5f, 10,1700);
//                addMovementToQueue("Stopping", 0, 0, 5,500);
//                return;
//            }

            /*
            The code below is used for the ultrasone sensors
             */

            if (this.DISTANCE.getDistance() < 20 && this.DISTANCE.getPulse() > 0 && this.moveDirection == MoveDirection.FORWARDS) {
                //calculate braking speed depending on DISTANCE to obstacle
                int brakingSpeed = (int) ((1500 - this.DISTANCE.getPulse()) / 50);


                addMovementToQueue("Braking", 0, 0, brakingSpeed, 500);
                return;
            }

            this.acceleration = Math.max(1, this.acceleration);
            this.acceleration = Math.min(30, this.acceleration);


            MOTOR.setAcceleration(this.acceleration);
            if (this.moveDirection == MoveDirection.FORWARDS) {
                MOTOR.setMove(1, 0);
            } else if (this.moveDirection == MoveDirection.BACKWARDS) {
                MOTOR.setMove(-1, 0);
            } else if (this.moveDirection == MoveDirection.LEFT) {
                MOTOR.setMove(0, 0.5f);
            } else if (this.moveDirection == MoveDirection.RIGHT) {
                MOTOR.setMove(0, -0.5f);
            } else if (this.moveDirection == MoveDirection.STATIONARY) {
                MOTOR.setMove(0, 0);
            }
        }
    }

    private void addMovementToQueue(String name, float targetSpeed, float targetAngle, int acceleration, int duration) {
        Movement movement = new Movement(name, targetSpeed, targetAngle, acceleration, duration);
        MOVEMENT_QUEUE.add(movement);
    }

    @Override
    public void reset() {
        MOVEMENT_QUEUE.clear();
        isExecutingMovement = false;

        this.moveDirection = MoveDirection.STATIONARY;
        this.acceleration = 5;
    }

    @Override
    public void onRemoteButtonPressed(int code) {
        if (code == Config.REMOTE_CHANNEL_PLUS) {

            // Move forwards
            // If we are moving backwards, stop
            if (this.moveDirection == MoveDirection.BACKWARDS) {
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = MoveDirection.FORWARDS;
            }

        } else if (code == Config.REMOTE_CHANNEL_MIN) {

            // Move backwards
            // If we are moving forwards, stop
            if (this.moveDirection == MoveDirection.FORWARDS) {
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = MoveDirection.BACKWARDS;
            }

        } else if (code == Config.REMOTE_VOLUME_PLUS) {

            // Move to the right
            // If we are moving to the left, stop
            if (this.moveDirection == MoveDirection.LEFT) {
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = MoveDirection.RIGHT;
            }

        } else if (code == Config.REMOTE_VOLUME_MIN) {

            // Move to the left
            // If we are moving to the right, stop
            if (this.moveDirection == MoveDirection.RIGHT) {
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = MoveDirection.LEFT;
            }
        } else if (code == Config.REMOTE_STOP) {
            this.moveDirection = MoveDirection.STATIONARY;
        } else if (code == Config.REMOTE_FORWARDS) {
            this.acceleration += 1;
        } else if (code == Config.REMOTE_BACKWARDS) {
            this.acceleration -= 1;
        }
    }

    @Override
    public void onBluetoothMessage(String input) {
        input = input.toLowerCase();

        if (input.equals("w")) {

            // Move forwards
            // If we are moving backwards, stop
            if (this.moveDirection == MoveDirection.BACKWARDS) {
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = MoveDirection.FORWARDS;
            }

        } else if (input.equals("s")) {

            // Move backwards
            // If we are moving forwards, stop
            if (this.moveDirection == MoveDirection.FORWARDS) {
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = MoveDirection.BACKWARDS;
            }

        } else if (input.equals("d")) {

            // Move to the right
            // If we are moving to the left, stop
            if (this.moveDirection == MoveDirection.LEFT) {
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = MoveDirection.RIGHT;
            }

        } else if (input.equals("a")) {

            // Move to the left
            // If we are moving to the right, stop
            if (this.moveDirection == MoveDirection.RIGHT) {
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = MoveDirection.LEFT;
            }
        } else if (input.equals(" ")) {
            this.moveDirection = MoveDirection.STATIONARY;
        } else if (input.equals("+")) {
            this.acceleration += 1;
        } else if (input.equals("-")) {
            this.acceleration -= 1;
        }
    }
}


