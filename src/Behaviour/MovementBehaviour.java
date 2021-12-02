package Behaviour;

import Configuration.Config;
import Hardware.Switch;
import Logic.InfraredLogic;
import Logic.MotorLogic;
import TI.Timer;

import java.util.ArrayList;

public class MovementBehaviour implements Behaviour {

    private MotorLogic motor;
    private InfraredLogic infraredLogic;

    private Switch leftWhisker;
    private Switch rightWhisker;

    private Timer timer;

    private ArrayList<Movement> movementQueue = new ArrayList<Movement>();
    private boolean isExecutingMovement;

    public MovementBehaviour(MotorLogic motorLogic, InfraredLogic infraredLogic) {
        this.motor = motorLogic;
        this.infraredLogic = infraredLogic;
    }

    private MoveDirection moveDirection = MoveDirection.Stationary;
    private float acceleration = 5;

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
                addMovementToQueue("Braking", 0, 0, 100, 500);
                addMovementToQueue("Backing up", -0.5f, 0, 5, 1500);
                addMovementToQueue("Stopping", 0, 0, 5,500);
                return;
            }

            if (this.rightWhisker.getState()) {
                addMovementToQueue("Braking", 0, 0, 100,500);
                addMovementToQueue("Backing up for left turn", -0.5f, 0, 5,3000);
                addMovementToQueue("Turn left", 0, 0.5f, 10,1700);
                addMovementToQueue("Stopping", 0, 0, 5,500);

                return;

            }

            if (this.leftWhisker.getState()) {
                addMovementToQueue("Braking", 0, 0, 100,500);
                addMovementToQueue("Backing up for right turn", -0.5f, 0,  5,3000);
                addMovementToQueue("Turn right", 0, -0.5f, 10,1700);
                addMovementToQueue("Stopping", 0, 0, 5,500);
                return;

            }


            int remoteCode = this.infraredLogic.getLastCode();

            if(remoteCode == Config.REMOTE_CHANNEL_PLUS) {
                this.moveDirection = MoveDirection.Forwards;
            } else if(remoteCode == Config.REMOTE_CHANNEL_MIN) {
                this.moveDirection = MoveDirection.Backwards;
            } else if(remoteCode == Config.REMOTE_VOLUME_PLUS) {
                this.moveDirection = MoveDirection.Right;
            } else if(remoteCode == Config.REMOTE_VOLUME_MIN) {
                this.moveDirection = MoveDirection.Left;
            } else if(remoteCode == Config.REMOTE_STOP) {
                this.moveDirection = MoveDirection.Stationary;
            }

            if(remoteCode == Config.REMOTE_FORWARDS) {
                this.acceleration += 1;
                this.infraredLogic.reset();
            } else if(remoteCode == Config.REMOTE_BACKWARDS) {
                this.acceleration -= 1;
                this.infraredLogic.reset();
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

        //System.out.println("Resetting movement target");
    }
}


